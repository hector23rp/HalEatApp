package com.lpro.haleater;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import id.zelory.compressor.Compressor;

public class CameraActivity extends AppCompatActivity {

    private TextureView textureView;    //Vista donde se visualiza la cámara.
    private ImageButton takePictureButton;  //Botón de la cámara para realizar la foto
    private ImageButton menuButton; //Botón para acceder al panel lateral de la pantalla.
    private DrawerLayout mDrawerLayout; //Layout principal
    private String cameraId; //ID de la camara
    private Size imageDimension;    //Dimensiones que soporta la cámara.
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private CameraDevice cameraDevice; //Representa a la cámara del dispositivo.
    protected CaptureRequest.Builder captureRequestBuilder;
    private Surface surface;
    protected CameraCaptureSession cameraCaptureSessions; //Sesión de la camara
    private Handler mBackgroundHandler;
    private File actualImage;   //Fichero donde se guarda la imagen que captura la cámara.
    private ProgressDialog dialog;

    private static String KEY_RESULT = "KEY_RESULT";    //Clave de el parámetro que indica si comes bien o no.
    private static String KEY_RESULT_WEEK = "KEY_RESULT_WEEK";    //Clave de el parámetro que indica si comes bien o no en lo que llevas de semana.
    private static String KEY_NAME = "KEY_NAME";    //Clave de el parámetro que indica el nombre del alimento.
    private static String KEY_PROTEINAS = "KEY_PROTEINAS";  //Clave de el parámetro proteinas.
    private static String KEY_CALORIAS = "KEY_CALORIAS";  //Clave de el parámetro calorias.
    private static String KEY_HIDRATOS = "KEY_HIDRATOS";  //Clave de el parámetro hidratos.
    private static String KEY_GRASAS = "KEY_GRASAS";  //Clave de el parámetro grasas.
    private static String KEY_AZUCAR = "KEY_AZUCAR";  //Clave de el parámetro azucar.

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /*
    * Listener de la vista donde se visualiza la cámara.
     */
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // Transform you image captured size according to the surface width and height
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };

    /*
    * Callback correspondiente a la cámara conectada. Registra cuando nos conectamos o desconectamos a ella o cuando se produce un error.
     */
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        textureView = (TextureView) findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(textureListener);
        //Definimos los botones de la pantalla.
        takePictureButton = (ImageButton) findViewById(R.id.btn_takepicture);
        menuButton = (ImageButton) findViewById(R.id.btn_menu);
        //Agregamos los listener de los correspondientes botones.
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity();
            }
        });
    }

    /*
    * Llama a la actividad MainActivity. Esta función se debe llamra cuando se pulsa el botón menuButton.
     */
    private void launchMainActivity() {
        closeCamera();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /*
    * Coge le id de la cámara trasera, la dimension del stream soportado por esta y realiza la conexión con la cámara.
     */
    public void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            StreamConfigurationMap map = null;
            for (String mCameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);

                // No utilizamos la cámara delantera
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }
                cameraId = mCameraId;
                break;
            }
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    * Cierra la conexión con la cámara.
     */
    private void closeCamera() {
        try {
            if (null != cameraCaptureSessions) {
                cameraCaptureSessions.close();
                cameraCaptureSessions = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        }
    }

    /*
    * Crea la previsualización de la cámara en la pantalla.
     */
    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            surface = new Surface(texture);
            setRequest();
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (null == cameraDevice) {
                        return;
                    }
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    * Crea el objeto captureRequestBuilder necesario para tomar imágenes.
     */
    protected void setRequest() {
        try {
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    * Captura la imagen de la pantalla de la cámara. Esta función se llama cuando se pulsa el botón takePictureButton.
     */
    protected void takePicture() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            // Orientation
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            actualImage = new File(Environment.getExternalStorageDirectory() + "/food.jpg");
            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Log.i("image", "in OnImageAvailable");
                    FileOutputStream fos = null;
                    Bitmap bitmap = null;
                    Image img = null;
                    try {
                        img = reader.acquireLatestImage(); //Cogemos la imagen del objeto ImageReader.
                        if (img != null) {
                            Image.Plane[] planes = img.getPlanes();
                            if (planes[0].getBuffer() == null) {
                                return;
                            }
                            int width = img.getWidth();
                            int height = img.getHeight();
                            ByteBuffer buffer = planes[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                            save(bytes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (null != fos) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (null != bitmap) {
                            bitmap.recycle();
                        }
                        if (null != img) {
                            img.close();
                        }

                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(actualImage);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                            compressImage();
                            sendToServer();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comprime la imagen que captura la cámara.
     */
    public void compressImage(){
        File actualImage = new File(Environment.getExternalStorageDirectory() + "/food.jpg");
        File compressedImage = new Compressor.Builder(this)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setDestinationDirectoryPath(Environment.getExternalStorageDirectory() + "/Haleat")
                .build()
                .compressToFile(actualImage);
        deleteImage(Environment.getExternalStorageDirectory() + "/food.jpg");
    }

    /**
     * Elimina el fichero especificado por pathFile.
     * @param pathFile
     */
    public void deleteImage(String pathFile){
        File file = new File(pathFile);
        boolean deleted = file.delete();
    }


    /**
     * Envía la información del usuario al servidor para registrarse.
     */
    public void sendToServer(){
        new CameraActivity.PostClass(this).execute(Environment.getExternalStorageDirectory() + "/Haleat/food.jpeg");
    }

    /**
     * Clase encargada de realizar la comunicación con el servidor.
     */
    public class PostClass extends AsyncTask<String, Void, String> {

        private final Context context;

        public PostClass(Context c) {
            this.context = c;
        }

        protected void onPreExecute() {
            dialog= new ProgressDialog(context);
            dialog.setMessage("Cargando...");
            dialog.show();
        }

        protected String doInBackground(String... params){
            String result = "";
            File f = new File(params[0]);

            Future uploading = Ion.with(CameraActivity.this)
                    .load("http://haleat.com/api/newfood")
                    .setHeader("authorization",TokenSaver.getToken(context))
                    .setMultipartFile("image", f)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            dialog.dismiss();
                            try {
                                if(result.getHeaders().code() == 200) {
                                    JSONObject jobj = new JSONObject(result.getResult());
                                    getMessage(jobj);
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
            return params[0];
        }

        protected void onPostExecute(String result){
            //deleteImage(result);
        }

    }

    /**
     * Inicia la actividad de ResultUser.
     */
    public void launchResultUser(){

    }

    /**
     * Realiza una acción dependiendo de lo que el servidor ha devuelto.
     */
    public void getMessage(JSONObject jobj){
            boolean result = true;
            if(jobj.length()<2){    //Si solo se recibe el parámetro mensaje, significa que el token ha expirado.
                launchLoginActivity();
            }
            else{
                String name = null;
                String proteinas = null;
                String calorias = null;
                String hidratos = null;
                String grasas = null;
                String azucar = null;
                String checkDay = null;
                String checkWeek = null;
                JSONArray arrayJson = new JSONArray();
                JSONObject jsonCheck = new JSONObject();
                try {
                    arrayJson = jobj.getJSONArray("nameFood");
                    jsonCheck = jobj.getJSONObject("diet");
                    checkDay = jsonCheck.getString("day");
                    checkWeek = jsonCheck.getString("week");
                    proteinas = jobj.getString("proteinas");
                    calorias = jobj.getString("kcal");
                    hidratos = jobj.getString("hidratosC");
                    grasas = jobj.getString("grasas");
                    calorias = jobj.getString("kcal");
                    azucar = jobj.getString("azucar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject json = null;
                try {
                    json = arrayJson.getJSONObject(0).getJSONObject("pred");
                    name = json.getString("nombre").replace("_"," ");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getBaseContext(), CameraResult.class);
                if(checkDay.equals("bad")){
                    intent.putExtra(KEY_RESULT, false);
                }
                else{
                    intent.putExtra(KEY_RESULT, true);
                }
                if(checkWeek.equals("bad")){
                    intent.putExtra(KEY_RESULT_WEEK, false);
                }
                else{
                    intent.putExtra(KEY_RESULT_WEEK, true);
                }
                intent.putExtra(KEY_NAME, name);
                intent.putExtra(KEY_PROTEINAS, proteinas);
                intent.putExtra(KEY_CALORIAS, calorias);
                intent.putExtra(KEY_HIDRATOS, hidratos);
                intent.putExtra(KEY_GRASAS, grasas);
                intent.putExtra(KEY_AZUCAR, azucar);
                closeCamera();
                startActivity(intent);
                finish();
            }
        }

    /**
     * Inicia la actividad Login.
     */
    public void launchLoginActivity(){
        closeCamera();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
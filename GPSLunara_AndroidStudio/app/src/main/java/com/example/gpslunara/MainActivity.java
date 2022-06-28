package com.example.gpslunara;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // define quem faz a solicitação da permissão: permissões do Manifesto
    private static final int PERMISSION_ALL  = 1;
    String[] PERMISSIONS=  {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    // variáveis de controle da tela
    private TextView txtLatitude;
    private TextView txtLongitude;

    // variaveis para uso do gps
    private LocationManager lManager;
    public Double lat;
    public Double lgt;

    /*
    * Procedimento para checar se tem permissão para fazer algo ou não
    * */
    public static boolean hasPermission(Context context, String...permissions){
        // passagem de parâmetro: ver se contexto é valido e se tem string
        if((context !=null) && (permissions !=null)){
            for (String permission: permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }


            }
        }
        return true;
        }
        @Override
        public void onRequestPermissionsResult(int RequestCode, String[] permissions, int[] grantResult) {
        super.onRequestPermissionsResult(RequestCode, permissions, grantResult);
        switch (RequestCode){
            case PERMISSION_ALL:
                for (String permission: permissions){
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        startGPS();
                    }
                }
             break;
            default:
                break;
        }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude=(TextView)  findViewById(R.id.txtlat);
        txtLongitude=(TextView)  findViewById(R.id.txtlong);

        // pede permissões no ciclo de vida
        if (!hasPermission(this, PERMISSIONS)){
         ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
               startGPS();
        }
    }

    public void startGPS()
    {
        Log.e("GPS", "Iniciou o GPS");
        //ligando o gps por typecast
        lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // vendo se o gps está ativo
        if(lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
          System.out.println("GPS ativo");
            Log.e("GPS", "GPS ativo");
        }

        // vendo se a rede está no ar
        if(lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            System.out.println("Rede ativo");
            Log.e("GPS", "Rede ativo");
        }
        // método principal, o onLocationChange
        LocationListener lListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
                switch (status) {
                    case LocationProvider.OUT_OF_SERVICE:
                        txtLatitude.setText("Sem serviço");
                        break;

                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        txtLatitude.setText("Indisponível");
                        break;
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
          lat = location.getLatitude();
          lgt = location.getLongitude();
          txtLatitude.setText(lat.toString());
          txtLongitude.setText(lgt.toString());
                SpannableStringBuilder builder = new SpannableStringBuilder(());
                String str1 = "Lat: ";
                String str2 = lat.toString();
                Spannable sp1 = new SpannableString(str1 + str2);
                sp1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp1.setSpan(new RelativeSizeSpan(1.3f), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp1.setSpan(new ForegroundColorSpan(Color.RED), str1.length(),str1.length()+str2.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
builder.append(sp1);
txtLatitude.setText(builder, TextView.BufferType.SPANNABLE);
txtLongitude.setText(MudarTexto("Long:", lgt.toString())),
                TextView.BufferType.SPANNABLE);
            }

            public SpannableStringBuilder MudarTexto (String str1, String str2){
                SpannableStringBuilder builder = new SpannableStringBuilder(());
                Spannable sp1 = new SpannableString(str1 + str2);
                sp1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp1.setSpan(new RelativeSizeSpan(1.3f), 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sp1.setSpan(new ForegroundColorSpan(Color.RED), str1.length(),str1.length()+str2.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(sp1);
                return builder;
            }

        };

        // Vou ativar o Listener para que me excute:

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            // Se estiver habilitado o gps execute o atualizados das coordenadas
            // o listener escuta as coordenadas do gps
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);

        }
       // verifico se tem permissão para habilitar a rede, dou permissão para escutar a rede
        if(lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            // Se estiver habilitado o gps execute o atualizados das coordenadas
            // o listener escuta as coordenadas do gps
            lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lListener);

        }
    }
}
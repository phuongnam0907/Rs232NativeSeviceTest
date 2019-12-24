package com.phuongnam.rs232;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.styl.uartmgrd.IUartmgrd;

import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {

    private IUartmgrd uartmgrd;
    private static final String TAG = "UartManager";

    TextView text;
    Button baudrate, open, close, send, read, write;
    private int fd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NativeBind();

        text = findViewById(R.id.text);
        baudrate = findViewById(R.id.butSetbaud);
        open = findViewById(R.id.open);
        close = findViewById(R.id.close);
        send = findViewById(R.id.send);
        read = findViewById(R.id.read);
        write = findViewById(R.id.write);
    }

    @Override
    protected void onStart() {
        super.onStart();

        baudrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    text.setText(text.getText() + "\nSet Baurate 115200...");
                    uartmgrd.setSerialPortParams(115200, 8, 1, 'n');
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText() + "\nOpen Port /dev/ttyHSL1");
                try {
                    fd = uartmgrd.open("/dev/ttyHSL1");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText() + "\nClose!");
                try {
                    uartmgrd.close(fd);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(text.getText() + "\nSend \"LePhuongNam0.123\" \nGet data...");
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    byte[] buf = uartmgrd.read_data(2048, 100);
                    int n =buf.length;
                    if (n > 0){
                        text.setText("Size is " + n + " - Data: ");
                        for (int i = 0; i < n; i++) text.setText(text.getText() + new Character((char)buf[i]).toString());
                    }
                    else Log.e("uart", "No Data...");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] buf = "One#2$Thr33%".getBytes();
                text.setText("\nWrite: \"One#2$Thr33%\"");
                try {
                    uartmgrd.write_data(buf, buf.length);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void NativeBind(){
        IBinder binder = ServiceManager.getService("uartmgrd");
        if (binder != null) {
            try {
                binder.unlinkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        Toast.makeText(MainActivity.this, TAG + "uartmgrd died: reconnecting", Toast.LENGTH_LONG).show();
                        uartmgrd = null;
                    }
                }, 0);
            } catch (NoSuchElementException e) {
                Log.w(TAG, "death recipient already released");
            }

            uartmgrd = IUartmgrd.Stub.asInterface(binder);
        } else {
            Toast.makeText(MainActivity.this, TAG + "uartmgrd not found; trying again", Toast.LENGTH_SHORT).show();
        }
    }
}
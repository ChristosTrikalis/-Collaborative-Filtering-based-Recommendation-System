package myapp.com.mapsactivity;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
public class TestAndroid extends Activity
{
     EditText ID,Topk;
     private ArrayList<String> name = new ArrayList<>();
     private ArrayList<String> category = new ArrayList<>();
     private ArrayList<Double> longidude = new ArrayList<>();
     private ArrayList<Double>latidude = new ArrayList<>();
     private int topk,id;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_android);


        ImageButton button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ID =findViewById(R.id.userID);

                if( ID.getText().toString().length() == 0 )
                    ID.setError( "First field is required!" );
                Topk = findViewById(R.id.textTopK);
                id = Integer.parseInt(ID.getText().toString());
                topk = Integer.parseInt(Topk.getText().toString());
                if( ID.getText().toString().length() != 0 )
                {
                    TaskRunner runner = new TaskRunner();
                    runner.execute(null,null,null);
                }


            }

        });
    }
    private class TaskRunner extends AsyncTask<Void ,Void,Void>
    {
        DataOutputStream out = null;
        DataInputStream in = null;
        Socket socket=null;
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                socket = new Socket("192.168.9.17",9004);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());

                out.writeInt(id);
                out.writeInt(topk);

                boolean flag = in.readBoolean();
                while (flag)
                {
                    latidude.add(in.readDouble());
                    longidude.add(in.readDouble());
                    category.add(in.readUTF());
                    name.add(in.readUTF());
                    flag = in.readBoolean();
                }
            }catch (IOException e){e.printStackTrace();}
            return  null;
        }
        @Override
        protected void onPostExecute(Void v)
        {
            try{
                out.close();
                in.close();
                socket.close();
            }catch (IOException e){e.printStackTrace();}

            Intent myIntent = new Intent(TestAndroid.this, MapsActivity.class);
            myIntent.putExtra("K",latidude.size());
            for(int i=0; i<topk; i++)
            {
                myIntent.putExtra("lat"+i,latidude.get(i));
                myIntent.putExtra("lon"+i,longidude.get(i));
                myIntent.putExtra("cat"+i,category.get(i));
                myIntent.putExtra("nm"+i,name.get(i));
            }
            startActivity(myIntent);

        }
    }
}

package michaelmcmullin.sda.firstday.utils;

import static android.support.v4.content.ContextCompat.getSystemService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Class for internet checking. Taken from https://stackoverflow.com/a/27312494/5233918
 */
public class Network extends AsyncTask<Void,Void,Boolean> {

  private Consumer mConsumer;
  public  interface Consumer { void accept(Boolean internet); }

  public  Network(Consumer consumer) { mConsumer = consumer; execute(); }

  @Override protected Boolean doInBackground(Void... voids) { try {
    Socket sock = new Socket();
    sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
    sock.close();
    return true;
  } catch (IOException e) { return false; } }

  @Override protected void onPostExecute(Boolean internet) { mConsumer.accept(internet); }
}

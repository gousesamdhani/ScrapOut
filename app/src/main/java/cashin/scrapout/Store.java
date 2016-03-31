package cashin.scrapout;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sammy on 05/03/16.
 */
public class Store {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Store(Context context) {
        sharedPreferences = context.getSharedPreferences("UserLog", 0);
        editor = sharedPreferences.edit();
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return sharedPreferences.getString(key, null);
    }

}

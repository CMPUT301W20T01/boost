//package ca.ualberta.boost;
//
//import android.content.Context;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.AsyncTask;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class geoLocate extends AsyncTask<String, Void, List<Address>> {
//
//    private Context context;
//
//    public geoLocate(Context context){
//        context = context;
//    }
//
//    @Override
//    protected List<Address> doInBackground(String... searchStrings) {
//        String searchString = searchStrings[0];
//        Geocoder geocoder = new Geocoder(context);
//        List<Address> results = new ArrayList<>();
//        try{
//            results = geocoder.getFromLocationName(searchString, 1);
//        }catch (IOException e){
//            // show error message that location was not found
//            // THIS never happens even if no location shows up ?
//            Toast.makeText(context, "geocoder" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        return results;
//    }
//
//}

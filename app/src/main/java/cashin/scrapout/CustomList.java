package cashin.scrapout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Sammy on 18/03/16.
 */
public class CustomList extends ArrayAdapter<String> {

    private String[] orderID;
    private String[] orderMaterials;
    private Integer[] status;
    private Activity context;
    boolean flag = true;
    //private boolean active;

    public CustomList(Activity context, String[] orderID, String[] orderMaterials, Integer[] status) {
        super(context, R.layout.list_layout, orderID);
        this.context = context;
        this.orderID = orderID;
        this.orderMaterials = orderMaterials;
        this.status = status;
        //this.active = active;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.list_layout, parent, false);
        final TextView cancel = (TextView) listViewItem.findViewById(R.id.cancel);
        final TextView order_number = (TextView) listViewItem.findViewById(R.id.orderID);
        final TextView materials = (TextView) listViewItem.findViewById(R.id.materials);

        cancel.setText(getContext().getResources().getString(status[position]));
        order_number.setText(orderID[position]);
        materials.setText(orderMaterials[position]);

        if(status[position] == R.string.done) {
            cancel.setTextColor(Color.parseColor("#8BC34A"));
        }
        else if(status[position] == R.string.cancelled) {
            TextView label = (TextView) listViewItem.findViewById(R.id.materialsLbl);
            ImageView recycle = (ImageView) listViewItem.findViewById(R.id.recycle);
            recycle.setImageResource(R.drawable.recycle_bw);
            label.setTextColor(Color.parseColor("#B6B6B6"));
            cancel.setTextColor(Color.parseColor("#B6B6B6"));
            materials.setTextColor(Color.parseColor("#B6B6B6"));
        }

        if(status[position] == R.string.cancel) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String orderID = order_number.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure want to cancel order # " + orderID + "?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            cancelOrder(orderID, cancel, position);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
        return  listViewItem;
    }

    private void cancelOrder(final String orderID, final TextView cancel, final int position) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setIndeterminate(true);
        dialog.setMessage("cancelling order");
        dialog.show();
        Store store = new Store(getContext());
        final HttpMethods httpMethods = new HttpMethods();
        final RequestParamsBuilder reqParams = new RequestParamsBuilder();
        reqParams.addHeader("accesstoken", store.get("accesstoken"));
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = httpMethods.get(reqParams.getHeaderParams(), "/users/orders/cancel/"+orderID);
                    final int responseCode = Integer.parseInt(response.get("responseCode").toString());
                    Activity a = (Activity) getContext();
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            if(responseCode >=200 && responseCode <=300) {
                                cancel.setText(getContext().getResources().getString(R.string.cancelled));
                                cancel.setClickable(false);
                                status[position] = R.string.cancelled;
                            } else {
                                Utils.displayToast(getContext(), "Unable to cancel order");
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        thread.start();
    }
}

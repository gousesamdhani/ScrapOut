package cashin.scrapout.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import cashin.scrapout.CustomList;
import cashin.scrapout.Order;
import cashin.scrapout.Orders;
import cashin.scrapout.R;
import cashin.scrapout.Utils;
/**
 * Created by Sammy on 21/03/16.
 */
public class ThreeFragment extends Fragment {
    ArrayList<Order> cancelled_orders;
    ArrayList<String> orderID;
    ArrayList<String> orderMaterials;
    ListView list;
    View view;
    boolean flag = true;

    public ThreeFragment() {
        // Required empty public constructor
    }

    public ThreeFragment(Orders orders) {
        this.cancelled_orders = orders.getCancelledOrders();
        orderID = new ArrayList<String>();
        orderMaterials = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(flag) {
            flag = false;
            view = inflater.inflate(R.layout.fragment_three, container, false);
            list = (ListView) view.findViewById(R.id.cancelledOrders);
            prepareMenu();
            //CustomList customList = new CustomList(this.getActivity(), orderID.toArray(new String[orderID.size()]),
              //      orderMaterials.toArray(new String[orderMaterials.size()]), false);
         //   list.setAdapter(customList);
        }
        return view;
    }

    private void prepareMenu() {
        Order order;
        for(int i=0; i<cancelled_orders.size(); i++) {
            order = cancelled_orders.get(i);
            orderID.add(order.getOrder_number());
            orderMaterials.add(order.getName());
        }
    }
}

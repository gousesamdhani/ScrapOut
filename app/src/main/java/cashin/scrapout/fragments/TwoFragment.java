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


public class TwoFragment extends Fragment{

    ArrayList<Order> all_orders;
    ArrayList<String> orderID;
    ArrayList<String> orderMaterials;
    ListView list;
    boolean flag = true;
    View view;

    public TwoFragment() {
        // Required empty public constructor
    }

    public TwoFragment(Orders orders) {
        this.all_orders = orders.getCompletedOrders();
        orderID = new ArrayList<String>();
        orderMaterials = new ArrayList<String>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(flag) {
            flag = false;
            view = inflater.inflate(R.layout.fragment_two, container, false);
            list = (ListView) view.findViewById(R.id.allOrders);
            prepareMenu();
            CustomList customList ;//= new CustomList(this.getActivity(), orderID.toArray(new String[orderID.size()]),
                    //orderMaterials.toArray(new String[orderMaterials.size()]), false);
            //list.setAdapter(customList);
        }
        return view;
    }

    private void prepareMenu() {
        Order order;
        for(int i=0; i<all_orders.size(); i++) {
            order = all_orders.get(i);
            orderID.add(order.getOrder_number());
            orderMaterials.add(order.getName());
        }
    }
}

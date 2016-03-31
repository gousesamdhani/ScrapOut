package cashin.scrapout.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import cashin.scrapout.CustomList;
import cashin.scrapout.Order;
import cashin.scrapout.Orders;
import cashin.scrapout.R;


public class OneFragment extends Fragment{

    ArrayList<String> orderID;
    ArrayList<String> orderMaterials;
    ArrayList<Order> all_orders;
    ArrayList<Integer> status;
    ListView list;
    View view;
    boolean flag = true;
    private int[] text = {R.string.cancel, R.string.done, R.string.cancelled};

    public OneFragment() {
        // Required empty public constructor
    }

    public OneFragment(Orders orders) {
        this.all_orders = orders.getOrders();
        orderID = new ArrayList<String>();
        orderMaterials = new ArrayList<String>();
        status = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(flag) {
            flag = false;
            view = inflater.inflate(R.layout.fragment_one, container, false);
            list = (ListView) view.findViewById(R.id.activeOrders);
            prepareMenu();
            CustomList customList = new CustomList(this.getActivity(), orderID.toArray(new String[orderID.size()]),
                    orderMaterials.toArray(new String[orderMaterials.size()]), status.toArray(new Integer[status.size()]));
            list.setAdapter(customList);
            return view;
        }
        return null;
    }

    private void prepareMenu() {
        Order order;
        for(int i=0; i<all_orders.size(); i++) {
            order = all_orders.get(i);
            orderID.add(order.getOrder_number());
            orderMaterials.add(order.getName());
            status.add(text[Integer.parseInt(order.getDeal_status())]);
        }
    }

}

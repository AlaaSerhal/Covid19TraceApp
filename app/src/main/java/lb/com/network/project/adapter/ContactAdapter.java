package lb.com.network.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import lb.com.network.project.R;
import lb.com.network.project.model.Contact;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private int ViewResourceId;
    static class ViewHolder {
        TextView uuid;
        TextView distance;

    }

    private ArrayList<Contact> nearByContacts;

    public ContactAdapter(Context context, int ViewResourceId,
                          ArrayList<Contact> nearByContacts) {
        super();
        this.context = context;
        this.ViewResourceId = ViewResourceId;
        this.nearByContacts = nearByContacts;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return nearByContacts.size();
    }

    @Override
    public Contact getItem(int position) {
        // TODO Auto-generated method stub
        return nearByContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup par) {

        // assign the view we are converting to a local variable
        View v = convertView;

        ViewHolder holder = null;
        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(ViewResourceId, null);
            holder = new ViewHolder();
            holder.uuid =(TextView) v.findViewById(R.id.uuid);
            holder.distance =(TextView) v.findViewById(R.id.distance);

            v.setTag(holder);
        } else {
            holder = (ContactAdapter.ViewHolder) v.getTag();
        }

        Contact contact = nearByContacts.get(position);

        holder.uuid.setText(contact.getUuid());
        holder.distance.setText(String.format("%.3f", contact.getDistance()));

        return v;

    }

}
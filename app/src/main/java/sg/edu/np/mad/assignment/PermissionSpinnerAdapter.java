package sg.edu.np.mad.assignment;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class PermissionSpinnerAdapter extends ArrayAdapter<String> {
    private List<String> permissionList;
    private int hideItemPosition;

    public PermissionSpinnerAdapter(Context context, int resource, List<String> permissionList) {
        super(context, resource, permissionList);
        this.permissionList = permissionList;
    }

    public void setItemToHide(int itemToHide)
    {
        this.hideItemPosition = itemToHide;
    }

}

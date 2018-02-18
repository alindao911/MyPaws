package charles09.alindao.com.mypaws.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import charles09.alindao.com.mypaws.R;

/**
 * Created by Pc-user on 18/02/2018.
 */

public class ViewPagerAdapter  extends PagerAdapter{
    private String[] list;
    private String[] name;
    private LayoutInflater layoutInflater;
    private Context ctx;

    public ViewPagerAdapter(Context ctx, String[] list, String[] name) {
        this.ctx = ctx;
        this.list = list;
        this.name = name;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.layout_model_viewpager_pets,container,false);
        ImageView petImage = itemView.findViewById(R.id.imgviewpetpic);
        TextView petName = itemView.findViewById(R.id.textPetName);

        UniversalImageLoader.setImage(list[position], petImage, null, "");
        petName.setText(name[position]);

        ViewPager pager = (ViewPager) container;
        pager.addView(itemView, 0);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager pager = (ViewPager) container;
        View view = (View) object;
        pager.removeView(view);
    }
}

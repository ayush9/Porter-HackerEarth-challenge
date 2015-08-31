package com.example.geek.porter;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.HttpStatus;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
public class CustomAdapter1 extends BaseAdapter{   
	ArrayList<HashMap<String, String>> result;
    Context context;
    ImageView imdb;int posi;
    ArrayList<Bitmap> imageId= new ArrayList<Bitmap>();
    
      private static LayoutInflater inflater=null;
 //   public CustomAdapter1(MainActivity mainActivity, ArrayList<HashMap<String, String>> prgmNameList, ArrayList<Bitmap> prgmImages) {
      public CustomAdapter1(MainActivity mainActivity, ArrayList<HashMap<String, String>> prgmNameList) {  
      // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
       // imageId=prgmImages;
         inflater = ( LayoutInflater )context.
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         for(int i=0;i<100;i++)
         {
         	imageId.add(null);
         }
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv,tv1,tv2;
        ImageView img;
        RatingBar rb;
        
    }
    Holder holder;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        holder=new Holder();
        View rowView;       
             rowView = inflater.inflate(R.layout.program_list, null);
             holder.tv=(TextView) rowView.findViewById(R.id.textView1);
             holder.tv1=(TextView) rowView.findViewById(R.id.price);
            // holder.rb=(RatingBar)rowView.findViewById(R.id.rating);
           //  holder.tv2=(TextView) rowView.findViewById(R.id.rating);
             holder.img=(ImageView) rowView.findViewById(R.id.imageView1); 
           //  imdb=(ImageView) rowView.findViewById(R.id.imageView1); 
             //String ss=result.get(position).get("rating");
             //float ff=Float.parseFloat(ss);
             //holder.rb.setRating(ff);
       //      ss=ss+"*";
         holder.tv.setText(result.get(position).get("name"));
         holder.tv1.setText("Rs. "+result.get(position).get("price"));
    //     holder.tv2.setText(ss);
       // new GetImage().execute(result.get(position).get("imagee"),Integer.toString(position));
 
        //   if(imageId.get(position)!=null)
      //  posi=position.;
         
        // holder.img.setImageBitmap(imageId.get(position)); 
        if (holder.img != null) {
			new ImageDownloaderTask(holder.img).execute(result.get(position).get("imagee"),Integer.toString(position));
		}
        if(imageId.get(position)!=null)
        {
        	holder.img.setImageBitmap(imageId.get(position));

        }
       
  rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent in = new Intent(context,
                        MyActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.putExtra("hashMap", result.get(position));
               context.startActivity(in);
            }
        });   
      rowView.setOnLongClickListener(new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			
			return false;
		}
	});
        return rowView;
    }
    
    
    
    
    
    
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        
    int posi;
    private final WeakReference<ImageView> imageViewReference;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }
        @Override
        protected Bitmap doInBackground(String... params) {
            posi=Integer.parseInt(params[1]);
        	return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
            	ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        imageId.set(posi, bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_launcher);
                        imageView.setImageDrawable(placeholder);
                       // imageId.addAll((Collection<? extends Bitmap>) placeholder);
                    }
                }
            }
        }
        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
    
    
    
    
    
    class GetImage extends AsyncTask<String, int[], Bitmap> {

	    @Override
	    protected Bitmap doInBackground(String... params) {
	     
	      Bitmap mIcon11 = null;
	      try {
	        InputStream in = new java.net.URL(params[0]).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	          Log.e("Error", e.getMessage());
	          e.printStackTrace();
	      }

	      // Get your image bitmap here

	      return mIcon11;
	    }

	    @Override
	    protected void onPostExecute(Bitmap bitmapResult) {
	      super.onPostExecute(bitmapResult);
	      // Add your image to your view
	      imdb.setImageBitmap(bitmapResult);
	      imageId.add(bitmapResult);
	    }
	}

}

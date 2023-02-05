package com.pasindu.postureinspector;

import static org.opencv.core.Core.NORM_MINMAX;
import static org.opencv.core.Core.addWeighted;
import static org.opencv.core.Core.bitwise_not;
import static org.opencv.core.Core.normalize;
import static org.opencv.core.Mat.zeros;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.FloatProperty;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.android.Utils;
import org.opencv.imgproc.Imgproc;
import org.opencv.osgi.OpenCVInterface;
import java.lang.Math;

import java.io.IOException;
import java.lang.reflect.Array;

public class RecordFragment extends Fragment {
    View view;
    static Mat canvas = new Mat(1000,1200,CvType.CV_8UC4);

    Point[] pointsL = {
            new Point(412, 563),
            new Point(406, 693),
            new Point(315, 862-40),
            new Point(398, 854-40),
            new Point(415, 432),
            new Point(415, 322),
            new Point(394, 215),
            new Point(306, 215),
            new Point(343, 325),
            new Point(348, 698),
            new Point(346, 572),
            new Point(336, 438),
            new Point(301, 704),
            new Point(277, 578),
            new Point(257, 454),
            new Point(262, 331)
    };

    Point[] pointsR = {
            new Point(805, 563),
            new Point(810, 693),
            new Point(901, 862-40),
            new Point(822, 854-40),
            new Point(803, 432),
            new Point(806, 322),
            new Point(824, 215),
            new Point(913, 215),
            new Point(879, 325),
            new Point(869, 698),
            new Point(874, 572),
            new Point(883, 438),
            new Point(919, 704),
            new Point(941, 578),
            new Point(962, 454),
            new Point(955, 331)
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_record, container, false);

        Button testButtonOne = view.findViewById(R.id.btnTestOne);
        TextView textView = view.findViewById(R.id.txtOne);
        ImageView imageView = view.findViewById(R.id.heatmapOne);
        testButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = "https://www.google.com";
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                textView.setText("Response is: " + response.substring(0,500));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("That didn't work!");
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                Bundle result = new Bundle();
                result.putString("df1","massage from fragment one");
                getParentFragmentManager().setFragmentResult("dataFrom1",result);
                textView.setText("Message sent to fragment two");
                //                    Mat img = Utils.loadResource(getActivity(), R.drawable.foots4, CvType.CV_8UC4);
                Mat img = new Mat(1000,1200,CvType.CV_8UC3);
                Mat temp = new Mat(1000,1200,CvType.CV_8UC4);
                Bitmap bitmap = Bitmap.createBitmap(1200,1000,Bitmap.Config.RGB_565);
                for (int i = 0; i < 16; i += 1){
                    temp = new Mat(1000,1200,CvType.CV_8UC3);
                    temp = drawCircle(temp,pointsL[i],120);
                    Imgproc.blur(temp,temp,new Size(50,50));
                    addWeighted(img, 1, temp, 1, 0,img);
                }
                for (int i = 0; i < 16; i += 1){
                    temp = new Mat(1000,1200,CvType.CV_8UC3);
                    temp = drawCircle(temp,pointsR[i],120);
                    Imgproc.blur(temp,temp,new Size(50,50));
                    addWeighted(img, 1, temp, 0.8, 0,img);
                }
                temp = new Mat(1000,1200,CvType.CV_8UC3);
                Imgproc.applyColorMap(img,temp,Imgproc.COLORMAP_JET);
                Utils.matToBitmap(temp,bitmap);
                imageView.setImageBitmap(bitmap);
            }
        });

        getParentFragmentManager().setFragmentResultListener("dataFrom2", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                String data = result.getString("df2");
                textView.setText(data);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getParentFragmentManager().clearFragmentResultListener("dataFrom2");
    }

    private Bitmap matToBitmap(Mat mat){
        Bitmap bitmap = Bitmap.createBitmap(mat.cols(),mat.rows(),Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat,bitmap);
        return bitmap;
    }

    private Mat drawCircle(Mat mat,Point center,int r){
        Mat img = mat;
        Scalar color = new Scalar(255, 255, 255, 100);
        Point point = new Point(0,0);
        Point _point = new Point(0,0);
        for (int i = 0; i < 360; i += 2) {
            double theta = i*Math.PI/180;
            point = new Point(center.x + (r*Math.sin(theta)),center.y + (r*Math.cos(theta)));
            if (i%5 == 0){
                Imgproc.line(mat, center, point,color,1,Imgproc.LINE_4);
            } else {
                _point = new Point(center.x + (r*Math.sin(theta)/10),center.y + (r*Math.cos(theta)/10));
                Imgproc.line(mat, _point, point,color,1,Imgproc.LINE_4);
            }
        }
        return img;
    }

    private float euclidean_distance(Point center, Point point, int radius){
        float distance = (float) Math.sqrt(Math.pow(center.x - point.x, 2) + Math.pow(center.y - point.y, 2));
        if (distance > radius) return 0;
        return distance;
    }

    private Mat drawGradientCircle(int r,int midIntensity){
        int radius = r;
        Mat gradient = Mat.zeros(2*r, 2*r,CvType.CV_8UC3);

        Point center = new Point(150, 200);
        Point point = new Point();

        for(int row=0; row<2*r; ++row){
            for(int col=0; col<2*r; ++col){
                point.x = col;
                point.y = row;
                gradient.put(row,col,euclidean_distance(center, point, radius));
            }
        }
        normalize(gradient, gradient, 0, midIntensity, NORM_MINMAX, CvType.CV_8UC3);
        bitwise_not(gradient, gradient);

        return gradient;
    }
}
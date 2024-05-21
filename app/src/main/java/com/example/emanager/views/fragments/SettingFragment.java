package com.example.emanager.views.fragments;

import static androidx.core.content.ContextCompat.getSystemService;
import static io.realm.Realm.getApplicationContext;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.anychart.scales.DateTime;
import com.example.emanager.R;
import com.example.emanager.databinding.FragmentSettingBinding;
import com.example.emanager.utils.DatabaseHelper;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String CHANNEL_ID = "CHANNEL_1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentSettingBinding binding;
    private View root;
    Context context;
    private Switch darkModeSwitch;
    private LinearLayout btnEditProfileItem;
    private LinearLayout btnChangePasswordItem;
    private LinearLayout exportCsvItem;
    private LinearLayout exchangeRateItem;

    private static final int PERMISSION_REQUEST_CODE = 1;
    private DatabaseHelper dbHelper;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false);

        dbHelper = new DatabaseHelper(getActivity());

        createNotificationChannel();

        Init();
        OnClick();

        return root;
        //return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    private void Init(){
        root = binding.getRoot();
        context = getActivity();
        btnEditProfileItem = binding.editProfileItem;
        btnChangePasswordItem = binding.changePasswordItem;
        darkModeSwitch = root.findViewById(R.id.darkModeSwitch);
        exportCsvItem = root.findViewById(R.id.exportCsvItem);
        exchangeRateItem = root.findViewById(R.id.exchangeRateItem);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            darkModeSwitch.setChecked(true);
        }
    }

    private void OnClick(){

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Xử lý khi chuyển đổi chế độ tối
            if(isChecked){
                //Toast.makeText(requireContext(), "Chế độ nền tối: Bật", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                //Toast.makeText(requireContext(), "Chế độ nền tối: Tắt", Toast.LENGTH_SHORT).show();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btnEditProfileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of SecondFragment
                ProfileFragment profileFragment = new ProfileFragment();

                // Get the FragmentManager and start a transaction.
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.content, profileFragment) // replace the container with the new fragment
                        .addToBackStack(null) // add this transaction to the back stack
                        .commit(); // commit the transaction
            }
        });

        btnChangePasswordItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an instance of SecondFragment
                ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();

                // Get the FragmentManager and start a transaction.
                getParentFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.content, changePasswordFragment) // replace the container with the new fragment
                        .addToBackStack(null) // add this transaction to the back stack
                        .commit(); // commit the transaction
            }
        });

        exportCsvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    exportDataToExcel();
                } else {
                    requestPermission();
                }

            }
        });

        exchangeRateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // URL cần mở
                String url = "https://www.sacombank.com.vn/cong-cu/ty-gia.html";
                // Tạo Intent với action ACTION_VIEW
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Thiết lập URL cho Intent
                intent.setData(Uri.parse(url));
                // Bắt đầu activity mới với intent
                startActivity(intent);
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportDataToExcel();
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exportDataToExcel() {
        try{
            //createExampleData();

            Cursor cursor = dbHelper.getAllChiTieus();
            if (cursor == null || cursor.getCount() == 0) {
                Toast.makeText(getActivity(), "Không có dữ liệu để xuất", Toast.LENGTH_SHORT).show();
                return;
            }

            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("tbChiTieus");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("MaChiTieu");
            headerRow.createCell(1).setCellValue("MaNguoiDung");
            headerRow.createCell(2).setCellValue("Tien");
            headerRow.createCell(3).setCellValue("ThoiGian");
            headerRow.createCell(4).setCellValue("Loai");
            headerRow.createCell(5).setCellValue("GhiChu");

            int rowIndex = 1;
            while (cursor.moveToNext()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(cursor.getInt(cursor.getColumnIndex("MaChiTieu")));
                row.createCell(1).setCellValue(cursor.getString(cursor.getColumnIndex("MaNguoiDung")));
                row.createCell(2).setCellValue(cursor.getString(cursor.getColumnIndex("Tien")));
                row.createCell(3).setCellValue(cursor.getString(cursor.getColumnIndex("ThoiGian")));
                row.createCell(4).setCellValue(cursor.getString(cursor.getColumnIndex("Loai")));
                row.createCell(5).setCellValue(cursor.getString(cursor.getColumnIndex("GhiChu")));
            }
            cursor.close();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            String fileName = "ChiTieus_data_" + currentDateandTime.toString() + ".xlsx";
            File file = new File(context.getExternalFilesDir(null), fileName);
            //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                //Toast.makeText(getActivity(), "Dữ liệu được lưu ở thư mục download", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(),  "Dữ liệu được lưu ở " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                //Log.e("Thành công: ", "Dữ liệu được lưu ở " + file.getAbsolutePath());

                /*Bitmap bitmap = BitmapFactory.decodeResource(getResources (), R.mipmap.ic_launcher);
                Notification notification = new NotificationCompat.Builder(getActivity(), "CHANNEL_1")
                        .setContentTitle ("Title push notification")
                        .setContentText("Message push notification")
                        .setSmallIcon (R.drawable.ic_export_csv)
                        .setLargeIcon(bitmap)
                        .setColor (getResources().getColor(R.color.default_color))
                        .build();
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
                notificationManagerCompat.notify(getNotificationId(), notification);*/

            } catch (IOException e) {
                Toast.makeText(getActivity(), "Lỗi khi xuất file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }*/
        }
    }

    private void createExampleData(){
        try {

            ContentValues chiTieu1 = new ContentValues();
            chiTieu1.put("MaChiTieu", "1");
            chiTieu1.put("MaNguoiDung", "1");
            chiTieu1.put("Tien", 10000);
            chiTieu1.put("ThoiGian", "01/12/2023 12:15:30");
            chiTieu1.put("Loai", "1");
            chiTieu1.put("GhiChu", "chiTieu1");

            ContentValues chiTieu2 = new ContentValues();
            chiTieu2.put("MaChiTieu", "2");
            chiTieu2.put("MaNguoiDung", "1");
            chiTieu2.put("Tien", 15000);
            chiTieu2.put("ThoiGian", "01/5/2024 12:01:01");
            chiTieu2.put("Loai", "2");
            chiTieu2.put("GhiChu", "chiTieu2");

            ContentValues chiTieu3 = new ContentValues();
            chiTieu3.put("MaChiTieu", "3");
            chiTieu3.put("MaNguoiDung", "1");
            chiTieu3.put("Tien", 30000);
            chiTieu3.put("ThoiGian", "23/05/2024 15:15:15");
            chiTieu3.put("Loai", "3");
            chiTieu3.put("GhiChu", "chiTieu3");

            ArrayList<ContentValues> chiTieus = new ArrayList<>();
            chiTieus.add(chiTieu1);
            chiTieus.add(chiTieu2);
            chiTieus.add(chiTieu3);

            for (ContentValues chiTieu : chiTieus) {
                String msg = "";
                long resultAdd = dbHelper.insertUser(chiTieu.get("MaChiTieu").toString(), chiTieu.get("MaNguoiDung").toString(), Integer.parseInt(chiTieu.get("Tien").toString()), chiTieu.get("ThoiGian").toString(), chiTieu.get("Loai").toString(), chiTieu.get("GhiChu").toString());
                if (resultAdd == -1)
                {
                    msg = "Fail to Insert Record! MaChiTieu " + chiTieu.get("MaChiTieu");
                }
                else {
                    msg = "Insert record Sucessfully! MaChiTieu " + chiTieu.get("MaChiTieu");;
                }
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
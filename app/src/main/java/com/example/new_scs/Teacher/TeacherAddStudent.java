package com.example.new_scs.Teacher;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.new_scs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jxl.Workbook;

public class TeacherAddStudent extends AppCompatActivity {

    private final int CHOOSE_FILE_FROM_DEVICE = 1001;
    ImageView imageView;
    EditText studentName, fatherMobileNo, studentDateOfBirth;
    TextView textViewClass, textViewSection;
    Button button, buttonExcelFile;
    CircleImageView circleImageView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton fab;
    Bitmap bitmap;
    Uri imageURI;
    StorageReference storageReference;
    SharedPreferences sp;
    String classValue, sectionValue, schoolName;
    Workbook workbook;
    private int date, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_add_student);
        imageView = findViewById(R.id.image_view_calendar);
        studentName = findViewById(R.id.student_name);
        fatherMobileNo = findViewById(R.id.father_mobile_no);
        studentDateOfBirth = findViewById(R.id.student_dateofbirth);
        button = findViewById(R.id.add_student_data);
        textViewClass = findViewById(R.id.textView51);
        textViewSection = findViewById(R.id.textView52);
        circleImageView = findViewById(R.id.student_photo);
        fab = findViewById(R.id.add_image);
        buttonExcelFile = findViewById(R.id.button26);
        sp = getSharedPreferences("SCS", MODE_PRIVATE);
        classValue = sp.getString("class", null);
        sectionValue = sp.getString("section", null);
        schoolName = sp.getString("SchoolName", null);
        storageReference = FirebaseStorage.getInstance().getReference(schoolName).child("class" + classValue + sectionValue);

        textViewClass.setText("Class - " + classValue);
        textViewSection.setText("Section - " + sectionValue);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ImagePicker.Companion.with(TeacherAddStudent.this).cameraOnly().
////                        cropOval().createIntent();
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 0);
//                Toast.makeText(TeacherAddStudent.this, "Click Camera", Toast.LENGTH_SHORT).show();
//            }
//        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentName != null && fatherMobileNo.length() == 10 && studentDateOfBirth != null) {
                    addData();
                } else {
                    Toast.makeText(TeacherAddStudent.this, "Enter every fields and correct Mobile Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonExcelFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                String[] mimetypes =
                        {"application/vnd.ms-excel", // .xls
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
                        };
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, CHOOSE_FILE_FROM_DEVICE);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                date = calendar.get(Calendar.DATE);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TeacherAddStudent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month = month + 1;

                        if (month > 0 && month < 10 && date > 0 && date < 10) {
                            studentDateOfBirth.setText(year + "/0" + month + "/0" + date);
                        } else if (date > 0 && date < 10) {
                            studentDateOfBirth.setText(year + "/" + month + "/0" + date);
                        } else if (month > 0 && month < 10) {
                            studentDateOfBirth.setText(year + "/0" + month + "/" + date);
                        } else if (!(month > 0 && month < 10 && date > 0 && date < 10)) {
                            studentDateOfBirth.setText(year + "/" + month + "/" + date);
                        }
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });

        studentDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = TeacherAddStudent.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Calendar calendar = Calendar.getInstance();
                date = calendar.get(Calendar.DATE);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TeacherAddStudent.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        month = month + 1;

                        if (month > 0 && month < 10 && date > 0 && date < 10) {
                            studentDateOfBirth.setText(year + "/0" + month + "/0" + date);
                        } else if (date > 0 && date < 10) {
                            studentDateOfBirth.setText(year + "/" + month + "/0" + date);
                        } else if (month > 0 && month < 10) {
                            studentDateOfBirth.setText(year + "/0" + month + "/" + date);
                        } else if (!(month > 0 && month < 10 && date > 0 && date < 10)) {
                            studentDateOfBirth.setText(year + "/" + month + "/" + date);
                        }
                    }
                }, year, month, date);
                datePickerDialog.show();
            }
        });
    }

    private String getFileExtensionUri(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void addData() {
//        if (imageURI != null) {
//            storageReference = storageReference.child(fatherMobileNo.getText().toString() + "." + getFileExtensionUri(imageURI));
//            storageReference.putFile(imageURI);
//        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", studentName.getText().toString());
        map.put("mobile_no", fatherMobileNo.getText().toString());
        map.put("dateofbirth", studentDateOfBirth.getText().toString());
        map.put("class", classValue);
        map.put("section", sectionValue);
        map.put("SchoolName", schoolName);
        db.collection("Student").document().set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(TeacherAddStudent.this, "Student data added Succeeded", Toast.LENGTH_SHORT).show();
                studentDateOfBirth.setText("");
                studentName.setText("");
                fatherMobileNo.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherAddStudent.this, "Student data not added Succeeded", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//            imageURI = data.getData();
//            circleImageView.setImageURI(imageURI);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_FILE_FROM_DEVICE && resultCode == RESULT_OK) {

            String filePath = data.getData().getPath();
            File file = new File(filePath);

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                int rowCount = sheet.getPhysicalNumberOfRows();

                if (rowCount > 0) {
                    Row row = sheet.getRow(0);
                    Cell cell = row.getCell(0);
                    if (row.getCell(0).getStringCellValue().equals("Name") && row.getCell(1).getStringCellValue().equals("Mobile_No")
                            && row.getCell(2).getStringCellValue().equals("Date_of_Birth")) {

                        for (int r = 1; r < rowCount; r++) {
                            Row row1 = sheet.getRow(r);
                            Cell cell1 = row1.getCell(0);
                            Cell cell2 = row1.getCell(1);
                            Cell cell3 = row1.getCell(2);

                            Log.e("Value", cell1.getStringCellValue());
                            Log.e("2 Value", "" + String.valueOf(cell2.getNumericCellValue()).charAt(0) +
                                    String.valueOf(cell2.getNumericCellValue()).substring(2, 11));

                            String month = "";

                            switch (String.valueOf(cell3.getDateCellValue()).substring(4, 7)) {
                                case "Jan":
                                    month = "01";
                                    break;
                                case "Feb":
                                    month = "02";
                                    break;
                                case "Mar":
                                    month = "03";
                                    break;
                                case "Apr":
                                    month = "04";
                                    break;
                                case "May":
                                    month = "05";
                                    break;
                                case "Jun":
                                    month = "06";
                                    break;
                                case "Jul":
                                    month = "07";
                                    break;
                                case "Aug":
                                    month = "08";
                                    break;
                                case "Sep":
                                    month = "09";
                                    break;
                                case "Oct":
                                    month = "10";
                                    break;
                                case "Nov":
                                    month = "11";
                                    break;
                                case "Dec":
                                    month = "12";
                                    break;

                            }
                            Log.e("3 Value", "" + String.valueOf(cell3.getDateCellValue()).substring(30) + "/" + month + "/" + String.valueOf(cell3.getDateCellValue()).substring(8, 10));
                            Log.e("3 Value", "" + cell3.getDateCellValue());

                            Map<String, Object> map = new HashMap<>();
                            map.put("name", cell1.getStringCellValue());
                            map.put("mobile_no", String.valueOf(cell2.getNumericCellValue()).charAt(0) +
                                    String.valueOf(cell2.getNumericCellValue()).substring(2, 11));
                            map.put("dateofbirth", String.valueOf(cell3.getDateCellValue()).substring(30) + "/" + month + "/" +
                                    String.valueOf(cell3.getDateCellValue()).substring(8, 10));
                            map.put("class", classValue);
                            map.put("section", sectionValue);
                            map.put("SchoolName", schoolName);

                            db.collection("Student").add(map);
                        }
                        Toast.makeText(TeacherAddStudent.this, "All Student Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Accessing another file", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(TeacherAddStudent.this, filePath, Toast.LENGTH_SHORT).show();

        }
    }

}
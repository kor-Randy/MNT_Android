package com.nexters.frutto.view.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nexters.frutto.R
import com.nexters.frutto.service.model.UserMissionResponse
import com.nexters.frutto.viewmodel.BackPressViewModel
import com.nexters.frutto.viewmodel.DoMissionViewModel
import java.io.*

class DoMissionActivity : AppCompatActivity()
{

    lateinit var doMissionViewModel: DoMissionViewModel
    lateinit var doMissionFragment : DoMissionFragment
    lateinit var doMissionFragment2 : DoMissionFragment2
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var fragmentManager: FragmentManager
    lateinit var backPressViewModel : BackPressViewModel
    val REQUEST_TEXT_GALLERY = 4
    val REQUEST_GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_do_mission)

        doMissionViewModel = ViewModelProvider(this)[DoMissionViewModel::class.java]
        backPressViewModel=  ViewModelProviders.of(this)[BackPressViewModel::class.java]

        fragmentManager=supportFragmentManager
        fragmentTransaction=fragmentManager.beginTransaction()
        doMissionFragment= DoMissionFragment()
        doMissionFragment2= DoMissionFragment2()

        doMissionViewModel.nowUserMission.value =
            intent.getParcelableExtra("nowUserMission") as UserMissionResponse


        doMissionViewModel.missionDescription =
            resources
            .getStringArray(R.array.arr_create_mission_des)[resources.getStringArray(R.array.arr_create_mission)
                                                        .indexOf(doMissionViewModel.nowUserMission.value!!.missionName)]


         setFrag(0)

    }

    fun setImage()
    {

        var permissionResult = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permissionResult == PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
        }
        else
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
            startActivityForResult(intent, REQUEST_TEXT_GALLERY)
        }


    }


    fun setFrag(n : Int)
    {
        fragmentTransaction = fragmentManager.beginTransaction()
        when(n)
        {
            0 ->
                {
                fragmentTransaction.replace(R.id.frag_do_mission,doMissionFragment)
                doMissionViewModel.fragmentNum=0
                fragmentTransaction.commit()

            }
            1->
            {
                if(doMissionViewModel.file==null && doMissionViewModel.missionText.value=="")
                {
                    Toast.makeText(this,"니또를 위해 미션을 작성해주세요 ㅜㅜ",Toast.LENGTH_LONG).show()
                }
                else
                {
                    fragmentTransaction.replace(R.id.frag_do_mission, doMissionFragment2)
                    doMissionViewModel.fragmentNum = 1
                    fragmentTransaction.commit()
                }
            }
        }
    }



    override fun onBackPressed() {

        when(doMissionViewModel.fragmentNum)
        {
            0->
            {
                finish()
            }
            1->
            {
                setFrag(0)
            }

        }

    }

    fun saveBitmapToJpeg(bitmap : Bitmap) : File
    {
        val storage = this.cacheDir
        val filename = System.currentTimeMillis().toString()+ ".jpg"
        var tempFile = File(storage,filename)

        try{
            tempFile.createNewFile()
            var out = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,out)
            out.close()


        } catch (e : FileNotFoundException)
            {
            Log.e("wlgusdnzzz",e.toString())
        } catch (e : IOException)
        {
            Log.e("wlgusdnzzz",e.toString())
        }

        doMissionViewModel.imageButtonText.value = filename
        return tempFile

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK)
        {

            if(data!=null) {
                val selectedImage = data?.getData()
                var filePathColumn: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
                var cursor = getContentResolver().query(
                    selectedImage,
                    filePathColumn, null, null, null
                )
                cursor.moveToFirst()

                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val picturePath = cursor.getString(column_index)

                cursor.close()

                val bmp = BitmapFactory.decodeStream(FileInputStream(picturePath), null, null)

                Log.d("wlgusdnzzz",bmp!!.toString())
                var height = bmp.height
                var width = bmp.width
                var bm : Bitmap?=null
                if(height>720) {
                    while (height > 720) {

                        bm = Bitmap.createScaledBitmap(bmp, (width * 720) / height, 720, true)
                        height = bm.getHeight()
                        width = bm.getWidth()
                    }
                }
                else
                {
                    bm=bmp
                }

                doMissionViewModel.file = saveBitmapToJpeg(bm!!)
                doMissionViewModel.bitmap = bm
            }

        }

    }



}
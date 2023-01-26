package com.example.putinder.sign_screen.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.models.UserInfo
import com.example.putinder.sign_screen.view_model.SignViewModel

private const val REQUEST_CODE = 3

class SignUpComposeFragment : Fragment() {

    interface Callbacks {
        fun onAuthPressed()
    }

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                UpdateUI()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signViewModel.userInfoLiveData.observe(
            viewLifecycleOwner,
            Observer {
                //onLoadFinish()
                if (it == null) {
                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = ContentActivity.newIntent(requireContext())
                    QueryPreferences.setStoredQuery(requireContext(), it.token, it.user.id)
                    startActivity(intent)
                }
            }
        )

//        signViewModel.photoIdLiveData.observe(
//            viewLifecycleOwner,
//            Observer {
//                photoId = it
//            }
//        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data?.data != null) {
                signViewModel.uploadPhoto(data.data!!)
            }
        }
    }

    @Composable
    private fun UpdateUI() {
        val nameValue = remember { mutableStateOf(TextFieldValue()) }
        val loginValue = remember { mutableStateOf(TextFieldValue()) }
        val passwordValue = remember { mutableStateOf(TextFieldValue()) }
        val confirmPasswordValue = remember { mutableStateOf(TextFieldValue()) }

        val photoId = signViewModel.photoIdLiveData.observeAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            
            UserImage()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                    contentDescription = "add_photo",
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                )
            }

            EditText(text = "Имя")

            EditText(text = "Логин")

            EditText(text = "Пароль")

            EditText(text = "Подтвердить пароль")

            Button(
                onClick = {
                    if (loginValue.value.text == "" || passwordValue.value.text == ""
                        || nameValue.value.text == "" || confirmPasswordValue.value.text == "") {
                        Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                    } else if (passwordValue.value.text != confirmPasswordValue.value.text){
                        Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                    } else {
                        //onLoadResume()
                        val userInfo = UserInfo(loginValue.value.text,
                            passwordValue.value.text,
                            nameValue.value.text,
                            photoId.value!!)
                        signViewModel.updateUserInfo(userInfo)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    text = "Зарегистрироваться",
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )
            }

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        callbacks?.onAuthPressed()
                    }
            ) {
                Text(
                    text = "Авторизоваться",
                )
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun EditText(text: String) {
        val stateValue = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            value = stateValue.value,
            onValueChange = {stateValue.value = it},
            placeholder = { Text(text = text) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
            ),
            maxLines = 1,
            singleLine = true,
        )
    }

    @Composable
    private fun loadPicture(uri: Uri?, placeholder: Painter? = null): Painter? {

        val state = remember {
            mutableStateOf(placeholder)
        }

        val context = LocalContext.current
        val result = object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(p: Drawable?) {
                state.value = placeholder
            }

            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?,
            ) {
                state.value = BitmapPainter(resource.asImageBitmap())
            }
        }
        try {
            Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(result)
        } catch (e: Exception) {
            //
        }
        return state.value
    }

    @Composable
    private fun UserImage() {
        val uri = signViewModel.userPhotoLiveData.observeAsState()
        val painter = loadPicture(
            uri = uri.value,
            placeholder = painterResource(id = R.drawable.img)
        )
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = "image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxHeight(0.3f)
            )
        }
    }

    @Preview
    @Composable
    private fun PreviewComposable() {
        UpdateUI()
    }

    companion object {
        fun newInstance() = SignUpComposeFragment()
    }

}
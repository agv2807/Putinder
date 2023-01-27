package com.example.putinder.sign_screen.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.view_model.SignViewModel
import kotlin.math.sign

class SignInComposeFragment : Fragment() {

    interface Callbacks {
        fun onRegPressed()
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
                signViewModel.loading.value = false
                if (it == null) {
                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                } else {
                    QueryPreferences.setStoredQuery(requireContext(), it.token, it.user.id)
                    val intent = ContentActivity.newIntent(requireContext())
                    startActivity(intent)
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun UpdateUI() {

        val loginValue = remember { mutableStateOf(TextFieldValue("akljsdn")) }
        val passwordValue = remember { mutableStateOf(TextFieldValue("klsamndkn")) }
        val loading = signViewModel.loading.observeAsState()

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = "image",
                    modifier = Modifier.fillMaxHeight(0.5f),
                    contentScale = ContentScale.Crop
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = loginValue.value,
                    onValueChange = {loginValue.value = it},
                    placeholder = { Text(text = "Логин")},
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    ),
                    maxLines = 1,
                    singleLine = true,
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = passwordValue.value,
                    onValueChange = {passwordValue.value = it},
                    placeholder = { Text(text = "Пароль")},
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
                    visualTransformation = PasswordVisualTransformation()
                )

                Button(
                    onClick = {
                        if (loginValue.value.text == "" || passwordValue.value.text == "") {
                            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                        } else {
                            signViewModel.loading.value = true
                            val userInfoAuth = UserInfoAuth(loginValue.value.text,
                                passwordValue.value.text)
                            signViewModel.updateAuthUserInfo(userInfoAuth)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        text = "Войти",
                        style = TextStyle(
                            fontSize = 16.sp,
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            callbacks?.onRegPressed()
                        }
                ) {
                    Text(
                        text = "Зарегистрироваться",
                    )
                }
            }
        }

        Loader(isLoad = loading.value!!)
    }

    @Composable
    private fun Loader(isLoad: Boolean) {
        if (isLoad) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

    companion object {
        fun newInstance() = SignInComposeFragment()
    }

    @Preview
    @Composable
    private fun ComposablePreview() {
        UpdateUI()
    }

}
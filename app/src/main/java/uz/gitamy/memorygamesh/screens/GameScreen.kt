package uz.gita.memory_game.ui.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymemorygame.repository.AppRepository
import uz.gita.memory_game.data.CardData
import uz.gita.memory_game.data.LevelEnum
import uz.gitamy.memorygamesh.R
import uz.gitamy.memorygamesh.databinding.ScreenGameBinding

class GameScreen : Fragment(R.layout.screen_game) {
    private val binding by viewBinding(ScreenGameBinding::bind)
    private var defLevel = LevelEnum.EASY
    private val args by navArgs<GameScreenArgs>()
    private val repository = AppRepository()
    private var _height = 0
    private var _width = 0
    private var count = 0
    private val images = ArrayList<ImageView>()
    private var first: ImageView? = null
    private var second: ImageView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        defLevel = args.level

        binding.space.post {
            _height = binding.container.height / defLevel.verCount
            _width = binding.container.width / defLevel.horCount
            val count = defLevel.horCount * defLevel.verCount
            val ls = repository.getData(count)
            describeCardData(ls)
        }
    }

    private fun describeCardData(ls: List<CardData>) {
        for (i in 0 until defLevel.horCount) {
            for (j in 0 until defLevel.verCount) {
                val image = ImageView(requireContext())
                binding.container.addView(image)
                val lp = image.layoutParams as ConstraintLayout.LayoutParams
                lp.apply {
                    width = _width
                    height = _height
                }
                lp.setMargins(4, 4, 4, 4)
//                image.x = i * _width * 1f
//                image.y = j * _height * 1f
                image.layoutParams = lp
                image.tag = ls[i * defLevel.verCount + j]
                //  image.setImageResource(ls[i* defLevel.verCount + j].imgRes)
                image.setImageResource(R.drawable.image_back)
                image.scaleType = ImageView.ScaleType.CENTER_CROP
                image.animate()
                    .x(i * _width * 1f)
                    .y(j * _height * 1f)
                    .setDuration(1000)
                    .start()
                images.add(image)
            }
        }

        addClickListener()
    }

    private fun addClickListener() {
        images.forEach { imageView ->
            imageView.setOnClickListener {
                if (first == null) {
                    first = imageView
                    first?.apply {
                        animate()
                            .setDuration(500)
                            .rotationY(89f)
                            .withEndAction {
                                val data = it.tag as CardData
                                setImageResource(data.imgRes)
                                rotationY = -89f
                                animate()
                                    .setDuration(500)
                                    .rotationY(0f)
                                    .withEndAction {
                                    }
                                    .start()
                            }
                            .start()
                    }
                }
                else if (second == null) {
                    if (first == imageView) {
                        first?.apply {
                            animate()
                                .setDuration(500)
                                .rotationY(-89f)
                                .withEndAction {
                                    setImageResource(R.drawable.image_back)
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                    rotationY = 89f
                                    animate()
                                        .setDuration(500)
                                        .rotationY(0f)
                                        .withEndAction {
                                        }
                                        .start()
                                }
                                .start()
                        }
                        first = null
                        return@setOnClickListener
                    }
                    second = imageView
                    second?.apply {
                        animate()
                            .setDuration(500)
                            .rotationY(89f)
                            .withEndAction {
                                val data = it.tag as CardData
                                setImageResource(data.imgRes)
                                rotationY = -89f
                                animate()
                                    .setDuration(500)
                                    .rotationY(0f)
                                    .withEndAction {
                                    }
                                    .start()
                            }
                            .start()
                    }

                    Handler(Looper.myLooper()!!).postDelayed({
                        if (first?.tag == second?.tag) {
                            first?.apply {
                                animate()
                                    .setDuration(500)
                                    .rotation(360f)
                                    .withEndAction {
                                        binding.container.removeView(first)
                                    }
                                    .start()
                            }
                            second?.apply {
                                animate()
                                    .setDuration(500)
                                    .rotation(360f)
                                    .start()
                                Handler(Looper.myLooper()!!).postDelayed({ visibility = View.GONE },
                                    500)
                            }
                            count += 2
                        }
                        else {
                            first?.apply {
                                animate()
                                    .setDuration(500)
                                    .rotationY(-89f)
                                    .withEndAction {
                                        setImageResource(R.drawable.image_back)
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                        rotationY = 89f
                                        animate()
                                            .setDuration(500)
                                            .rotationY(0f)
                                            .withEndAction {
                                            }
                                            .start()
                                    }
                                    .start()
                            }
                            second?.apply {
                                animate()
                                    .setDuration(500)
                                    .rotationY(-89f)
                                    .withEndAction {
                                        setImageResource(R.drawable.image_back)
                                        scaleType = ImageView.ScaleType.CENTER_CROP
                                        rotationY = 89f
                                        animate()
                                            .setDuration(500)
                                            .rotationY(0f)
                                            .withEndAction {
                                            }
                                            .start()
                                    }
                                    .start()
                            }
                        }
                        first = null
                        second = null
                        fin()
                    }, 1500)

                } else { }
            }
        }
    }

    private fun fin() {
        if (count == (defLevel.horCount * defLevel.verCount)) {
            Toast.makeText(requireContext(), "you win", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        }
    }

}
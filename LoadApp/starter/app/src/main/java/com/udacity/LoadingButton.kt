package com.udacity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0f
    private var heightSize = 0f
    private var animateWidth = 0f

    private val valueAnimator = ValueAnimator()
    private val path = Path()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }
    private var radius = 30.0f
    private val pointPosition: PointF = PointF(widthSize/2, heightSize-100)

    // initialize a Paint object with a handful of basic styles.
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        color= Color.RED
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    private var backgroundLoadingColor = 0
    private var circleColor = 0
    init {
        valueAnimator.duration=2000
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundLoadingColor = getColor(R.styleable.LoadingButton_backgroundLoadingColor,0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor,0)
//            fanSpeedLowColor = getColor(R.styleable.DialView_fanColor1, 0)
//            fanSpeedMediumColor = getColor(R.styleable.DialView_fanColor2, 0)
//            fanSeedMaxColor = getColor(R.styleable.DialView_fanColor3, 0)
        }
    }


    private fun colorizer() {
//        widthSize=500f
        var animator = ObjectAnimator.ofArgb(this,
            "backgroundColor", Color.BLACK, Color.RED)
        animator.setDuration(5000)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.RESTART
        animator.interpolator=LinearInterpolator()
        animator.start()
//        invalidate()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = backgroundLoadingColor
        canvas?.drawRect(20f,heightSize-400,widthSize,widthSize-20,paint)
        paint.color = Color.WHITE
        paint.textSize = 55.0f
        canvas?.drawText("Download", width/2f, height/2f, paint)
        if (buttonState == ButtonState.Clicked){
            paint.color = backgroundLoadingColor
            canvas?.drawText("Download", width/2f, height/2f, paint)
            paint.color = Color.WHITE
            paint.textSize = 33.0f
            canvas?.drawText("We are downloading", width/2f, height/2f, paint)          //value when we start download
            paint.color = circleColor
            canvas?.drawCircle((width / 1.2).toFloat(), (height / 2).toFloat(), radius, paint)    //show it when download starts

            drawAnimatedCircle()


            buttonState.next()
//            invalidate()              //maybe need it
        }else if(buttonState == ButtonState.Loading){
//            //animate circle
//            paint.color = backgroundLoadingColor
//            paint.textSize = 33.0f
//            canvas?.drawText("We are downloading", width/2f, height/2f, paint)          //value when we start download
//            paint.color = backgroundLoadingColor
//            canvas?.drawCircle((width / 1.2).toFloat(), (height / 2).toFloat(), radius, paint)    //show it when download starts
//            paint.color = backgroundLoadingColor
//            canvas?.drawText("Downloaded", width/2f, height/2f, paint)
            buttonState.next()            //return back to completed, then change text
//            invalidate()                  //to force call redraw so text changes to be "Downloaded"
        }
            else{
//            Toast.makeText(context,buttonState.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    private fun drawAnimatedCircle() {
        val circle = RectF((width / 1.2).toFloat(),heightSize/2,(height / 2).toFloat(),heightSize/2)
        path.arcTo(circle, 0f, 180f)
        path.arcTo(circle, 180f, 180f)
        val pathMeasure = PathMeasure(path,false)
        var valueAnimator2 = ValueAnimator.ofFloat(0f,pathMeasure.length)
        val pos = floatArrayOf(0f,0f)
        valueAnimator2.addUpdateListener(ValueAnimator.AnimatorUpdateListener {
            val v:Float = it.getAnimatedValue() as Float
            pathMeasure.getPosTan(v,pos,null)
        })
        valueAnimator2.duration=3000
        valueAnimator2.interpolator=LinearInterpolator()
        valueAnimator2.start()
    }

    override fun performClick(): Boolean {
        super.performClick()

        //change text of button, start animate, circle
        buttonState = buttonState.next()
        colorizer()

        invalidate()
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w *1f
        heightSize = h*1f
        setMeasuredDimension(w, h)
    }

}
package com.example.dicodingevent.ui.common

import android.text.Html
import android.text.Spanned
import android.widget.TextView

class Utils {

   companion object {
        fun renderHtmlString(textView: TextView, htmlString: String) {
           val result: Spanned =
               Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)

            // Set HTML formatted string to the TextView
           textView.text = result
       }
   }
}
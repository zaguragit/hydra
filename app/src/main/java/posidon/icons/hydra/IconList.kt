package posidon.icons.hydra

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import posidon.icons.hydra.tools.ThemeTools
import posidon.icons.hydra.tools.Tools
import posidon.icons.hydra.tools.copy
import posidon.icons.hydra.tools.dp
import posidon.icons.hydra.view.GridView

class IconList : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var searchBar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gridView = GridView(this).apply {
            numColumns = 4
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 72.dp.toInt(), 0, 8.dp.toInt())
            setFadingEdgeLength(56.dp.toInt())
            isVerticalFadingEdgeEnabled = true
            clipToPadding = false
        }
        searchBar = EditText(this@IconList).apply {
            hint = "Search icons"
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    (gridView.adapter as IconsAdapter).search(s.toString())
                }
            })
            setPadding(24.dp.toInt(), 0, 24.dp.toInt(), 0)
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 64.dp.toInt())
            setBackgroundColor(0xdd111213.toInt())
        }
        setContentView(FrameLayout(this).apply {
            addView(gridView)
            addView(searchBar)
        })
        window.decorView.setBackgroundColor(0x55111213)
        window.statusBarColor = 0xdd111213.toInt()

        gridView.adapter = IconsAdapter()
    }

    internal inner class IconsAdapter() : BaseAdapter() {

        val icons: ArrayList<String>
        val themeRes = packageManager.getResourcesForApplication(packageName)
        val searchResults = ArrayList<String>()

        init {
            icons = try { ThemeTools.getResourceNames(themeRes, packageName) }
                    catch (e: Exception) { ArrayList() }
            searchResults.addAll(icons)
        }

        fun search(term: String) {
            searchResults.clear()
            val searchOptimizedTerm = Tools.searchOptimize(term)
            for (string in icons) {
                if (Tools.searchOptimize(string).contains(searchOptimizedTerm)) {
                    searchResults.add(string)
                }
            }
            notifyDataSetChanged()
        }

        override fun getCount(): Int = searchResults.size
        override fun getItem(position: Int): Any? = null
        override fun getItemId(position: Int): Long = 0

        inner class ViewHolder(val icon: ImageView)

        override fun getView(i: Int, cv: View?, parent: ViewGroup): View? {
            var convertView = cv
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(this@IconList).inflate(R.layout.drawer_item, parent, false)
                viewHolder = ViewHolder(convertView.findViewById(R.id.iconimg))
                convertView.findViewById<View>(R.id.icontxt).visibility = View.GONE
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            val intRes = themeRes.getIdentifier(searchResults[i], "drawable", packageName)
            if (intRes == 0) {
                viewHolder.icon.setImageDrawable(null)
                viewHolder.icon.setOnClickListener(null)
            } else {
                val drawable = Tools.tryAnimate(themeRes.getDrawable(intRes, null))
                viewHolder.icon.setImageDrawable(drawable)
                viewHolder.icon.setOnClickListener {
                    BottomSheetDialog(this@IconList, R.style.bottomsheet).run {
                        setContentView(LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            gravity = Gravity.CENTER_HORIZONTAL
                            addView(ImageView(context).apply {
                                setImageDrawable(drawable.copy()?.let { d -> Tools.tryAnimate(d) })
                                layoutParams = ViewGroup.LayoutParams(185.dp.toInt(), 185.dp.toInt())
                                setPadding(0, 24.dp.toInt(), 0, 10.dp.toInt())
                            })
                            addView(TextView(context).apply {
                                text = searchResults[i]
                                textSize = 20f
                                setTextColor(0xffffffff.toInt())
                                setPadding(0, 10.dp.toInt(), 0, 24.dp.toInt())
                                gravity = Gravity.CENTER_HORIZONTAL
                            })
                        })
                        window!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(R.drawable.bottom_sheet)
                        show()
                    }
                }
            }
            return convertView
        }
    }
}
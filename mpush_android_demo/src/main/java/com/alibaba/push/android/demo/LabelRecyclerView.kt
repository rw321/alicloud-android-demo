package com.alibaba.push.android.demo

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.push.android.demo.databinding.LabelItemBinding

class LabelRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val labels = mutableListOf<String>()
    private val maxLabelCount: Int = 8
    private val labelAdapter: LabelAdapter
    var addLabelClickCallback: (() -> Unit)? = null
    var deleteLabelClickCallback: ((String) -> Unit)? = null
    var moreThanMaxLabelCountCallback:((Boolean)->Unit)? = null
    private var addBtnText: String

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LabelRecyclerView)
        addBtnText = ta.getString(R.styleable.LabelRecyclerView_addBtnText)?:context.getString(R.string.push_add_tag)
        ta.recycle()

        layoutManager = GridLayoutManager(context, 3, VERTICAL, false)
        addItemDecoration(GridSpacingItemDecoration(3, 8.toDp()))
        labelAdapter = LabelAdapter(mutableListOf<String>().apply {
            add(addBtnText)
        }).apply {
            addLabelCallback = {
                addLabelClickCallback?.invoke()
            }
            deleteLabelCallback = {
                deleteLabelClickCallback?.invoke(it)
            }
         }
        adapter = labelAdapter
    }

    fun addLabel(label: String) {
        if(labels.contains(label)) {
            context.toast(context.getText(R.string.push_already_add).toString())
            return
        }
        labels.add(0, label)
        updateLabel()
    }

    fun deleteLabel(label: String) {
        labels.remove(label)
        updateLabel()
    }

    private fun updateLabel(){
        if (labelAdapter.data.size > maxLabelCount) {
            labelAdapter.data.apply {
                clear()
                addAll(labels.subList(0, maxLabelCount))
                add(addBtnText)
            }
            labelAdapter.notifyItemRangeChanged(0 , 9)
        }else {
            labelAdapter.data.apply {
                val lastSize = size
                clear()
                labelAdapter.notifyItemRangeRemoved(0, lastSize)
                addAll(labels)
                add(addBtnText)
                labelAdapter.notifyItemRangeChanged(0, size)
            }
        }
        moreThanMaxLabelCountCallback?.invoke(labels.size > maxLabelCount)
    }
}

class LabelAdapter(val data: MutableList<String>) :
    RecyclerView.Adapter<LabelAdapter.ViewHolder>() {

    var deleteLabelCallback: ((String) -> Unit)? = null
    var addLabelCallback: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            LabelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvLabel.text = data[position]
        if (position == data.size - 1) {
            holder.itemView.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.push_add_label_bg)
            holder.binding.ivDelete.setImageResource(R.drawable.push_add)
            holder.binding.tvLabel.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.push_color_text_black
                )
            )
        } else {
            holder.itemView.background =
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.push_label_bg)
            holder.binding.ivDelete.setImageResource(R.drawable.push_delete)
            holder.binding.tvLabel.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.push_color_424FF7
                )
            )
        }
        holder.itemView.setOnClickListener {
            if (position == data.size - 1) {
                addLabelCallback?.invoke()
            } else {
                deleteLabelCallback?.invoke(data[position])
            }
        }
    }

    class ViewHolder(val binding: LabelItemBinding) : RecyclerView.ViewHolder(binding.root)

}

class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager

        with(outRect) {
            top = if (layoutManager.spanSizeLookup.getSpanGroupIndex(
                    itemPosition,
                    spanCount
                ) % spanCount == 0
            ) {
                // 左边列的item需要顶部间隔
                spacing
            } else {
                // 其他位置不设置间隔
                0
            }

            if (itemPosition % spanCount == 0) {
                // 顶行item需要左间隔
                left = 0
                right = 6.toDp()
            }else if (itemPosition % spanCount == 1){
                left = 3.toDp()
                right = 3.toDp()
            } else {
                left = 6.toDp()
                right = 0
            }

            // 所有的item都设置底部间隔
            bottom = spacing
        }
    }
}
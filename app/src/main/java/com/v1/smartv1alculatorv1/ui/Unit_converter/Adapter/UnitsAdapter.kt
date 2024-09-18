import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.ui.Unit_converter.model.ConverterModel
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class UnitsAdapter(
    private val context: Context,
    private val unitList: List<ConverterModel>,
    private val listener: (String, Int) -> Unit
) : RecyclerView.Adapter<UnitsAdapter.UnitViewHolder>() {

    // Track the selected position, load saved position from SharedPreferences
    private var selectedPosition = 0

    inner class UnitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitTextView: TextView = itemView.findViewById(R.id.text_unit)
        val nameTextView: TextView = itemView.findViewById(R.id.txt_full_name)

        init {
            itemView.setOnClickListener {
                val adapterPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedPosition) // Deselect previous item
                    selectedPosition = adapterPosition // Update selected position
                    notifyItemChanged(selectedPosition) // Select new item
                    // Lưu vị trí đã chọn vào SharedPreferences
                    // Trigger the listener, passing the unit name or code as a String
                    listener(unitList[adapterPosition].unit, adapterPosition)
                }
            }
        }

        fun bind(unit: ConverterModel) {
            unitTextView.text = unit.unit
            nameTextView.text = unit.name
            // Update the background based on whether this item is selected
            if (adapterPosition == selectedPosition) {
                itemView.setBackgroundResource(R.drawable.bg_border_8_unit_neutral)
            } else {
                itemView.setBackgroundResource(R.drawable.bg_border_8_unit_neutral_off)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_unit_converter, parent, false)
        return UnitViewHolder(view)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        val unit = unitList[position]
        holder.bind(unit) // Bind ConverterModel and handle the selection state
    }

    override fun getItemCount(): Int = unitList.size


    fun updatePosition(newPosition: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = newPosition
        notifyItemChanged(selectedPosition)
    }
}

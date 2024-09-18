package com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet

import UnitsAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.inteface.OnClickToLengthBottomSheet
import com.v1.smartv1alculatorv1.ui.Unit_converter.model.ConverterModel
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class LengthToBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedPosition: Int? = null
    private var listener: OnClickToLengthBottomSheet? = null
    private var ic_close : ImageView? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ic_close = view.findViewById(R.id.ic_close_length)
        ic_close?.setOnClickListener {
            dismiss()
        }
        selectedPosition = UnitPreferences.loadSavedPositionToLength(requireContext())
        val recyclerViewUnits: RecyclerView = view.findViewById(R.id.recyclerView)
        val unitList = listOf(
            ConverterModel("Kilometer", "km"),
            ConverterModel("Hectometer", "hm"),
            ConverterModel("Decameter", "dam"),
            ConverterModel("Meter", "m"),
            ConverterModel("Decimeter", "dm"),
            ConverterModel("Centimeter", "cm"),
            ConverterModel("Millimeter", "mm"),
            ConverterModel("Micrometer", "μm"),
            ConverterModel("Nanometer", "nm"),
            ConverterModel("Mile", "mi"),
            ConverterModel("Yard", "yd"),
            ConverterModel("Foot", "ft"),
            ConverterModel("Inch", "in")


        )

        recyclerViewUnits.layoutManager = LinearLayoutManager(context)
        val adapter = UnitsAdapter(requireContext(), unitList) { selectedUnit, index->
            UnitPreferences.saveToLengthUnit(requireContext(), selectedUnit)
            UnitPreferences.saveSelectedPositionToLength(requireContext(), index)
            listener?.onUnitToSelected(selectedUnit)
            Log.d("TAG123", "onViewCreated: $selectedUnit")
            //dismiss()
        }
        adapter.updatePosition(selectedPosition!!)
        recyclerViewUnits.adapter = adapter
    }
}
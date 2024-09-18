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
import com.v1.smartv1alculatorv1.inteface.OnClickFromLengthBottomSheet
import com.v1.smartv1alculatorv1.ui.Unit_converter.model.ConverterModel
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class VolumeFromBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedPosition: Int? = null
    private var ic_close : ImageView? = null
    private var listener: OnClickFromLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickFromLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        ic_close = view.findViewById(R.id.ic_close_volume)
        ic_close?.setOnClickListener {
            dismiss()
        }
        selectedPosition = UnitPreferences.loadSavedPositionFromVolume(requireContext())
        val recyclerViewUnits: RecyclerView = view.findViewById(R.id.recyclerView)
        val unitList = listOf(
            ConverterModel("Cubic Kilometer", "km³"),
            ConverterModel("Cubic Meter", "m³"),
            ConverterModel("Cubic Decimeter", "dm³"),
            ConverterModel("Cubic Centimeter", "cm³"),
            ConverterModel("Cubic Millimeter", "mm³"),
            ConverterModel("Liter", "l"),
            ConverterModel("Milliliter", "ml"),
            ConverterModel("Microgram", "μα"),
            ConverterModel("Cubic Inch", "in"),
            ConverterModel("Imperial Gallon", "imp gal"),
            ConverterModel("US Liquid Gallon", "US gal"),
            ConverterModel("Imperial fl oz", "imp fl oz"),
            ConverterModel("US fl oz", "US fl oz")
        )

        recyclerViewUnits.layoutManager = LinearLayoutManager(context)
        val adapter = UnitsAdapter(requireContext(), unitList) { selectedUnit, index->
            UnitPreferences.saveFromVolumeUnit(requireContext(), selectedUnit)
            UnitPreferences.saveSelectedPositionFromVolume(requireContext(), index)
            listener?.onUnitSelected(selectedUnit)
            Log.d("TAG123", "onViewCreated: $selectedUnit")
            //dismiss()
        }
        adapter.updatePosition(selectedPosition!!)
        recyclerViewUnits.adapter = adapter
    }

    override fun dismiss() {
        super.dismiss()
        listener?.onDismissListener()
    }
}
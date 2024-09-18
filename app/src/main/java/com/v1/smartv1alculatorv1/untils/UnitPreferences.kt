package com.v1.smartv1alculatorv1.untils

import android.content.Context
import android.content.SharedPreferences
import okhttp3.internal.cache2.Relay.Companion.edit

object UnitPreferences {

    private const val PREFS_NAME = "unit_preferences"
    private const val DEFAULT_LENGTH_UNIT = "M"
    private const val DEFAULT_VOLUME_UNIT = "M³"
    private const val DEFAULT_AREA_UNIT = "M²"
    private const val DEFAULT_MASS_UNIT = "Kg"
    private const val DEFAULT_TIME_UNIT = "S"
    private const val DEFAULT_SPEED_UNIT = "M/s"
    private const val DEFAULT_TEMP_UNIT = "°C"
    private const val DEFAULT_DENSITY_UNIT = "kg/m³"
    private const val DEFAULT_ENERGY_UNIT = "J"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Lưu và lấy đơn vị chiều dài
    fun saveFromLengthUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_lengthFrom_unit", unit)
        editor.apply()
    }

    fun getFromLengthUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_lengthFrom_unit", DEFAULT_LENGTH_UNIT) ?: DEFAULT_LENGTH_UNIT
    }

    fun saveToLengthUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_lengthTo_unit", unit)
        editor.apply()
    }

    fun getToLengthUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_lengthTo_unit", DEFAULT_LENGTH_UNIT) ?: DEFAULT_LENGTH_UNIT
    }
    // Lưu và lấy đơn vị thể tích
    fun saveFromVolumeUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_volumeFrom_unit", unit)
        editor.apply()
    }

    fun getFromVolumeUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_volumeFrom_unit", DEFAULT_VOLUME_UNIT) ?: DEFAULT_VOLUME_UNIT
    }

    fun saveToVolumeUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_volumeTo_unit", unit)
        editor.apply()
    }

    fun getToVolumeUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_volumeTo_unit", DEFAULT_VOLUME_UNIT) ?: DEFAULT_VOLUME_UNIT
    }
    // Lưu và lấy đơn vị diện tích
    fun saveFromAreaUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_areaFrom_unit", unit)
        editor.apply()
    }

    fun getFromAreaUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_areaFrom_unit", DEFAULT_AREA_UNIT) ?: DEFAULT_AREA_UNIT
    }
    fun saveToAreaUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_areaTo_unit", unit)
        editor.apply()
    }

    fun getToAreaUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_areaTo_unit", DEFAULT_AREA_UNIT) ?: DEFAULT_AREA_UNIT
    }
    // Lưu và lấy đơn vị khối lượng
    fun saveFromMassUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_massFrom_unit", unit)
        editor.apply()
    }

    fun getFromMassUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_massFrom_unit", DEFAULT_MASS_UNIT) ?: DEFAULT_MASS_UNIT
    }
    fun saveToMassUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_massTo_unit", unit)
        editor.apply()
    }

    fun getMassToUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_massTo_unit", DEFAULT_MASS_UNIT) ?: DEFAULT_MASS_UNIT
    }

    // Lưu và lấy đơn vị thời gian
    fun saveFromTimeUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_timeFrom_unit", unit)
        editor.apply()
    }

    fun getFromTimeUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_timeFrom_unit", DEFAULT_TIME_UNIT) ?: DEFAULT_TIME_UNIT
    }

    fun saveToTimeUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_timeTo_unit", unit)
        editor.apply()
    }

    fun getToTimeUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_timeTo_unit", DEFAULT_TIME_UNIT) ?: DEFAULT_TIME_UNIT
    }
    // Lưu và lấy đơn vị tốc độ
    fun saveFromSpeedUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_speedFrom_unit", unit)
        editor.apply()
    }

    fun getFromSpeedUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_speedFrom_unit", DEFAULT_SPEED_UNIT) ?: DEFAULT_SPEED_UNIT
    }

    fun saveToSpeedUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_speedTo_unit", unit)
        editor.apply()
    }

    fun getToSpeedUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_speedTo_unit", DEFAULT_SPEED_UNIT) ?: DEFAULT_SPEED_UNIT
    }
    // Lưu và lấy đơn vị nhiệt độ
    fun saveFromTempUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_tempFrom_unit", unit)
        editor.apply()
    }

    fun getFromTempUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_tempFrom_unit", DEFAULT_TEMP_UNIT) ?: DEFAULT_TEMP_UNIT
    }

    fun saveToTempUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_tempTo_unit", unit)
        editor.apply()
    }

    fun getToTempUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_tempTo_unit", DEFAULT_TEMP_UNIT) ?: DEFAULT_TEMP_UNIT
    }

    // Lưu và lấy đơn vị mật độ
    fun saveFromDensityUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_densityFrom_unit", unit)
        editor.apply()
    }

    fun getFromDensityUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_densityFrom_unit", DEFAULT_DENSITY_UNIT) ?: DEFAULT_DENSITY_UNIT
    }

    fun saveToDensityUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_densityTo_unit", unit)
        editor.apply()
    }

    fun getToDensityUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_densityTo_unit", DEFAULT_DENSITY_UNIT) ?: DEFAULT_DENSITY_UNIT
    }


    // Lưu và lấy đơn vị năng lượng
    fun saveFromEnergyUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_energyFrom_unit", unit)
        editor.apply()
    }

    fun getFromEnergyUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_energyFrom_unit", DEFAULT_ENERGY_UNIT) ?: DEFAULT_ENERGY_UNIT
    }

    fun saveToEnergyUnit(context: Context, unit: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putString("selected_energyTo_unit", unit)
        editor.apply()
    }

    fun getToEnergyUnit(context: Context): String {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getString("selected_energyTo_unit", DEFAULT_ENERGY_UNIT) ?: DEFAULT_ENERGY_UNIT
    }

    //possition
    fun saveSelectedPositionFromLength(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromLength(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position", 0)

    }
    fun saveSelectedPositionToLength(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionToLength(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To", 0)
    }

    //possitionVolume
    fun saveSelectedPositionFromVolume(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position1", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromVolume(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position1", 0)

    }
    fun saveSelectedPositionToVolume(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To1", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToVolume(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To1", 0)
    }

    //possitionArea
    fun saveSelectedPositionFromArea(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position2", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromArea(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position2", 0)

    }
    fun saveSelectedPositionToArea(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To2", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToArea(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To2", 0)
    }

    //possitionMass
    fun saveSelectedPositionFromMass(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position3", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromMass(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position3", 0)

    }
    fun saveSelectedPositionToMass(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To3", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToMass(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To3", 0)
    }

    //possitionTime
    fun saveSelectedPositionFromTime(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position3", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromTime(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position3", 0)

    }
    fun saveSelectedPositionToTime(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To3", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToTime(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To3", 0)
    }




    //possitionSpeed
    fun saveSelectedPositionFromSpeed(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position3", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromSpeed(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position3", 0)

    }
    fun saveSelectedPositionToSpeed(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To3", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToSpeed(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To3", 0)
    }

    //possitionTemp
    fun saveSelectedPositionFromTemp(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position3", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromTemp(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position3", 0)

    }
    fun saveSelectedPositionToTemp(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To3", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToTemp(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To3", 0)
    }

    //possitionDensity
    fun saveSelectedPositionFromDensity(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position3", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromDensity(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position3", 0)

    }
    fun saveSelectedPositionToDensity(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To3", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToDensity(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To3", 0)
    }

    //possitionEnergy
    fun saveSelectedPositionFromEnergy(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position3", position).apply()
        editor.apply()
    }

    // Function to load saved position from SharedPreferences
    fun loadSavedPositionFromEnergy(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position3", 0)

    }
    fun saveSelectedPositionToEnergy(context: Context, position: Int) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putInt("selected_position_To3", position).apply()
        editor.apply()
    }

    fun loadSavedPositionToEnergy(context: Context): Int {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getInt("selected_position_To3", 0)
    }



    // Reset tất cả các giá trị
    fun resetPreferences(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.clear() // Xóa tất cả dữ liệu trong SharedPreferences
        editor.apply()
    }




}

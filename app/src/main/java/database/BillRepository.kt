package database

import android.app.Application

class BillRepository(application: Application) {

    private var billDao: BillDao

    private var itemDao: ItemDao

    init {
        val db = BillDatabase.getInstance(application)
        billDao = db.billDao
        itemDao = db.itemDao
    }

    fun insert(bill: Bill): MutableList<Long> {
        val insertBillData = BillData(
            address = bill.address,
            companyName = bill.companyName,
            salesTax = bill.salesTax,
            time = bill.time
        )
        val billID = billDao.insert(insertBillData)
        val returnID = mutableListOf(billID)
        for (item in bill.items) {
            val insertItem = ItemData(
                name = item.name,
                billId = billID.toInt(),
                amount = item.amout,
                singlePrice = item.singlePrice,
                totalPrice = item.totalPrice
            )
            val itemID = itemDao.insert(insertItem)
            returnID.add(itemID)
        }
        return returnID
    }

    fun updateItem(billData: BillData): Int {
        return billDao.update(billData)
    }

    fun updateBill(itemData: ItemData): Int {
        return itemDao.update(itemData)
    }

    fun deleteItem(itemData: ItemData) {
        itemDao.delete(itemData)
    }

    fun deleteBill(billData: BillData) {
        itemDao.deleteByBillId(billData.id)
        billDao.delete(billData)
    }

    fun getItem(id: Int): ItemData {
        return itemDao.get(id)
    }

    fun getBillData(id: Int): BillData {
        return billDao.get(id)
    }

    fun getAllItemsFromBillId(billId: Int): List<ItemData> {
        return itemDao.getItemsByBillId(billId)
    }
}

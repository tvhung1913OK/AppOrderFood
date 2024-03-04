const mongoose = require('mongoose');
const OrderItemModel = require('../model/orderitem.model');
const RevenueModel = require('../model/revenue.model');

async function getDailyRevenueForStaff(req, res) {
    try {
        const staffId = req.query.staffId; 

        if (!staffId) {
            return res.status(400).json({ error: 'Thiếu tham số staffId.' });
        }

        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const endDate = new Date(today);
        endDate.setHours(23, 59, 59, 999);

        const orderItems = await OrderItemModel.find({
            idStaff: staffId,
            createdAt: { $gte: today, $lte: endDate },
        }).populate('idProduct');

        let totalRevenue = 0;

        for (const orderItem of orderItems) {
            if (orderItem.idProduct && orderItem.idProduct.price && typeof orderItem.idProduct.price === 'number') {
                let revenue = orderItem.quantity * orderItem.idProduct.price;
                totalRevenue += revenue;
            } else {
                console.log('Sản phẩm không hợp lệ:', orderItem.idProduct);
                console.log('quantity:', orderItem.quantity);
                if (orderItem.idProduct) {
                    console.log('price:', orderItem.idProduct.price);
                } else {
                    console.log('Sản phẩm không tồn tại hoặc price không được định nghĩa.');
                }
            }
        }

        
        const totalRevenueNumber = Number(totalRevenue);
        

        if (!isNaN(totalRevenueNumber) && isFinite(totalRevenueNumber)) {
            
            await RevenueModel.create({
                staffId: staffId,
                totalRevenue: totalRevenueNumber,
                date: today,
            });
        } else {
            
            return res.status(400).json({ error: 'Giá trị totalRevenue không hợp lệ.' });
        }

        return res.json({ totalRevenue: totalRevenueNumber });
    } catch (error) {
        console.error('Lỗi khi tính toán doanh thu của nhân viên:', error);
        return res.status(500).json({ error: 'Có lỗi xảy ra khi tính toán doanh thu của nhân viên.' });
    }
}

module.exports = {
  
  getDailyRevenueForStaff,
};

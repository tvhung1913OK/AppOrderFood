const mongoose = require('mongoose');

const orderDetail = new mongoose.Schema(
      {
            name: { type: String, required: true },
            status: { type: String, default: 'Pending' },
            totalPrice: { type: Number, required: true },
            idOrderItem: { type: [mongoose.Types.ObjectId], ref: 'orderItem', required: true },
      },
      { timestamps: true }
);
orderDetail.pre('save', async function (next) {
      try {
            console.log('OrderDetail before save:', this);
            const OrderItem = mongoose.model('orderItem');


            for (const orderItemId of this.idOrderItem) {
                  const orderItem = await OrderItem.findById(orderItemId);

                  if (orderItem) {

                        orderItem.status = 'confirm';
                        await orderItem.save();
                  }
            }



            console.log('OrderDetail after save:', this);
            next();
      } catch (error) {
            console.error('Lỗi trong pre-save hook:', error);
            next(error);
      }
});
module.exports = mongoose.model('order_detail', orderDetail);

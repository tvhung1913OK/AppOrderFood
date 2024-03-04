const mongoose = require('mongoose');

const OrderItemSchema = new mongoose.Schema({
  idProduct: { type: mongoose.Schema.Types.ObjectId, ref: 'product', required: true },
  idCustomer: { type: mongoose.Schema.Types.ObjectId, ref: 'customer', required: true },
  quantity: { type: Number, required: true },
  status: { type: String, default: 'unconfirm'}, 
  createdAt: { type: Date, default: Date.now },
});

const OrderItemModel = mongoose.model('orderItem', OrderItemSchema);

module.exports = OrderItemModel;

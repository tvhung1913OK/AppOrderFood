const OrderItemModel = require('../model/orderitem.model');

exports.getAllOrderItems = async (req, res) => {
    try {
        const orderItems = await OrderItemModel.find()
        .populate({
          path: 'idProduct',
          model: 'product',
          select: 'name quantity image price idDiscount',
           populate: {
            path: 'idDiscount',
            model: 'discount',
            select: 'name description discountPerson dateStart dateEnd',
        },
      })
            
            .populate({
                path: 'idCustomer',
                model: 'customer',
                select: 'name phone email address', 
                populate: {
                    path: 'idUser',
                    model: 'user',
                    select: 'name phone email address', 
                },
            });

        res.json(orderItems);
    } catch (error) {
        res.status(500).json({ error: 'Không thể lấy danh sách OrderItem' });
    }
};

exports.getOrderItemsByCustomerId = async (req, res) => {
  const { idCustomer } = req.params;

  try {
    const orderItems = await OrderItemModel.find({ idCustomer })
    .populate({
      path: 'idProduct',
      model: 'product',
      select: 'name quantity image price idDiscount',
       populate: {
        path: 'idDiscount',
        model: 'discount',
        select: 'name description discountPerson dateStart dateEnd',
    },
  })
      .populate({
        path: 'idCustomer',
        model: 'customer',
        select: 'name phone email address',
        populate: {
          path: 'idUser',
          model: 'user',
          select: 'name phone email address',
        },
      });

    res.json(orderItems);
  } catch (error) {
    res.status(500).json({ error: 'Không thể lấy danh sách OrderItem theo idCustomer' });
  }
};







// Thêm một OrderItem mới
exports.createOrderItem = async (req, res) => {
  const { idProduct, idCustomer, quantity,totalPrice } = req.body;

  try {
    const newOrderItem = new OrderItemModel({
      idProduct,
      idCustomer,
      totalPrice,
      quantity,
    });

    const savedOrderItem = await newOrderItem.save();
    res.status(201).json(savedOrderItem);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: 'Không thể tạo OrderItem mới' });
  }
};

// Sửa thông tin của một OrderItem
exports.updateOrderItem = async (req, res) => {
  const { id } = req.params;
  const { idProduct, idCustomer, quantity,totalPrice } = req.body;

  try {
    const updatedOrderItem = await OrderItemModel.findByIdAndUpdate(
      id,
      {
        idProduct,
        idCustomer,
        quantity,
        totalPrice,
      },
      { new: true }
    );

    if (!updatedOrderItem) {
      return res.status(404).json({ error: 'Không tìm thấy OrderItem' });
    }

    res.json(updatedOrderItem);
  } catch (error) {
    res.status(500).json({ error: 'Không thể cập nhật OrderItem' });
  }
};

// Xóa một OrderItem
exports.deleteOrderItem = async (req, res) => {
  const { id } = req.params;

  try {
    const deletedOrderItem = await OrderItemModel.findByIdAndRemove(id);

    if (!deletedOrderItem) {
      return res.status(404).json({ error: 'Không tìm thấy OrderItem' });
    }

    res.json({ message: 'OrderItem đã được xóa' });
  } catch (error) {
    res.status(500).json({ error: 'Không thể xóa OrderItem' });
  }
};


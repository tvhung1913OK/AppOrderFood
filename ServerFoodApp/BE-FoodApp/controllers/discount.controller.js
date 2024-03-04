const discount = require('../model/discount.model');
const moment = require('moment');

//[GET]
const getDiscount = async (req, res) => {
      try {
            const data = await discount.find({});
            if (data.length == 0) {
                  res.status(404).json('Data is empty');
            } else {
                  res.status(200).json(data);
            }
      } catch (error) {
            res.status(500).json(error);
      }
};

//[POST]
const createDiscount = async (req, res) => {
      try {
            if (!req.body) {
                  res.status(404).json('Dữ liệu không được để trống');
            } else {
                  if (moment(req.body.dateEnd).isValid()) {
                        const dateEnd = moment(req.body.dateEnd).format('L');
                        const newDiscount = await discount({ ...req.body, dateEnd }).save();
                        res.status(200).json(newDiscount);
                  } else {
                        res.status(404).json('Date không hợp lệ');
                  }
            }
      } catch (error) {
            res.status(500).json(error);
      }
};

//[PUT]
const updateDiscount = async (req, res) => {
      try {
            if (!req.body) {
                  res.status(404).json('Dữ liệu không được để trống');
            } else {
                  if (moment(req.body.dateEnd).isValid()) {
                        const dateEnd = moment(req.body.dateEnd).format('L');
                        const newDiscount = await discount.findByIdAndUpdate(
                              { _id: req.params.id },
                              { ...req.body, dateEnd },
                              { new: true, upsert: true }
                        );
                        res.status(200).json(newDiscount);
                  } else {
                        res.status(404).json('Date không hợp lệ');
                  }
            }
      } catch (error) {
            res.status(500).json(error);
      }
};

//[DEL]
const deleteDiscount = async (req, res) => {
      try {
            await discount.deleteOne({ _id: req.params.id });
            res.status(200).json('Delete completed!');
      } catch (error) {
            res.status(500).json(error);
      }
};

module.exports = {
      getDiscount,
      createDiscount,
      updateDiscount,
      deleteDiscount,
};

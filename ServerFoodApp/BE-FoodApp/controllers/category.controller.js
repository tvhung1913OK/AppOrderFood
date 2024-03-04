const Category = require("../model/category.model");

// Sử dụng biến Category ở đây

const addCategory = async (req, res) => {
  try {
      const { name, type } = req.body;

      // Kiểm tra xem name đã được cung cấp
      if (!name || !type) {
          return res.status(400).json({ error: "Tên danh mục là bắt buộc" });
      }

      const newCategory = new Category({ name, type });
      const savedCategory = await newCategory.save();
      res.status(201).json(savedCategory);
  } catch (error) {
      res.status(500).json({ error: error.message });
  }
};

// Lấy tất cả các danh mục
const getAllCategories = async (req, res) => {
  try {
      const categories = await Category.find();
      res.status(200).json(categories);
  } catch (error) {
      res.status(500).json({ error: error.message });
  }
};

// Xóa một danh mục theo ID
const deleteCategory = async (req, res) => {
    try {
      const { catId } = req.params;
  
      // Xóa danh mục theo ID và trả về thông tin danh mục đã xóa
      const deletedCategory = await Category.findByIdAndRemove(catId);
  
      if (!deletedCategory) {
        return res.status(404).json({ error: "Danh mục không tồn tại" });
      }
  
      res.status(204).send();
    } catch (error) {
      res.status(500).json({ error: error.message });
    }
  };


// Sửa danh mục theo ID
const editCategory = async (req, res) => {
    try {
        const { name, type } = req.body; // Lấy name từ phần thân yêu cầu
  
        // Trích xuất ID từ đường dẫn URL
        const { catId } = req.params;
  
        if (!name || !type) {
            return res.status(400).json({ error: "Vui lòng cung cấp tên danh mục mới" });
        }
  
        const updatedCategory = await Category.findByIdAndUpdate(catId, { name, type }, { new: true });
        res.status(200).json(updatedCategory);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
  };
  




module.exports = {
  addCategory,
  getAllCategories,
  deleteCategory,
  editCategory
};

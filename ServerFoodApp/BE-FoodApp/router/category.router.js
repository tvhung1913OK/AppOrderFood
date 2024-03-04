const express = require("express");
const router = express.Router();

const categortController = require("../controllers/category.controller")

router.post('/addCategory', categortController.addCategory)
router.get('/', categortController.getAllCategories)
router.put('/updateCategory/:catId', categortController.editCategory)
router.delete('/deleteCategory/:catId', categortController.deleteCategory)

module.exports = router;
package cn.vic.manager.controller;

import cn.vic.entity.Product;
import cn.vic.manager.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static Logger LOG = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product product) {
        LOG.info("创建产品，参数：{}", product);

        Product result = service.addProduct(product);

        LOG.info("创建产品，结果：{}", result);
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product findOne(@PathVariable String id) {
        LOG.info("查询单个产品，id={}", id);
        Product product = service.findOne(id);
        LOG.info("查询单个产品，结果={}", product);
        return product;
    }
}

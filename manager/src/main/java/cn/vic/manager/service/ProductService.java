package cn.vic.manager.service;

import cn.vic.entity.Product;
import cn.vic.entity.enums.ProductStatus;
import cn.vic.manager.repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品服务类
 */
@Service
public class ProductService {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository repository;

    public Product addProduct(Product product) {
        LOG.debug("创建产品，参数：{}", product);
        // 数据校验
        checkProduct(product);

        // 设置默认值
        setDefault(product);

        Product result = repository.save(product);

        LOG.debug("创建产品，结果：{}", result);
        return result;
    }

    /**
     * 设置默认值
     * 创建时间、更新时间
     * 投资步长、锁定期、状态
     *
     * @param product
     */
    private void setDefault(Product product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(new Date());
        }
        if (product.getUpdateAt() == null) {
            product.setUpdateAt(new Date());
        }
        if (product.getStepAmount() == null) {
            product.setStepAmount(BigDecimal.ZERO);
        }
        if (product.getLockTerm() == null) {
            product.setLockTerm(0);
        }
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.AUDITING.name());
        }
    }

    /**
     * 产品数据校验
     * 1. 非空
     * 2. 收益率0~30
     * 3. 投资步长为整数
     *
     * @param product
     */
    private void checkProduct(Product product) {
        Assert.isNull(product.getId(), "编号不可为空！");
        // 其他非空校验

        Assert.isTrue(BigDecimal.ZERO.compareTo(product.getRewardRate()) < 0 && BigDecimal.valueOf(30).compareTo(product.getRewardRate()) >= 0, "收益率范围错误！");
        Assert.isTrue(BigDecimal.valueOf(product.getStepAmount().longValue()).compareTo(product.getStepAmount()) == 0, "投资步长必须为整数！");
    }

    /**
     * 查询单个产品
     *
     * @param id 产品编号
     * @return 对应产品或者 null
     */
    public Product findOne(String id) {
        Assert.notNull(id, "需要产品编号参数！");
        LOG.debug("查询单个产品，id={}", id);
        Product product = repository.findOne(id);
        LOG.debug("查询单个产品，结果={}", product);
        return product;
    }
}

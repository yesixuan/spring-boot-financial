package cn.vic.manager.service;

import cn.vic.entity.Product;
import cn.vic.entity.enums.ProductStatus;
import cn.vic.manager.repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Assert.notNull(product.getId(), "编号不可为空！");
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

    /**
     * 分页查询产品
     *
     * @param idList
     * @param minRewardRate
     * @param maxRewardRate
     * @param statusList
     * @param pageable
     * @return
     */
    public Page<Product> query(List<String> idList,
                               BigDecimal minRewardRate, BigDecimal maxRewardRate,
                               List<String> statusList,
                               Pageable pageable) {
        LOG.debug("查询产品，idList={},minRewardRate={},maxRewardRate={},statusList={},pageable={}", idList, minRewardRate, maxRewardRate, statusList, pageable);

        Specification<Product> specification = new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression<String> idCol = root.get("id");
                Expression<BigDecimal> rewardRateCol = root.get("rewordRate");
                Expression<String> statusCol = root.get("status");
                List<Predicate> predicates = new ArrayList<>();

                /**
                 * 复杂查询，结合多个条件来生成查询条件
                 */
                if (idList != null && idList.size() > 0) {
                    predicates.add(idCol.in(idList));
                }
                if (BigDecimal.ZERO.compareTo(minRewardRate) < 0) {
                    predicates.add(cb.ge(rewardRateCol, minRewardRate));
                }
                if (BigDecimal.ZERO.compareTo(maxRewardRate) < 0) {
                    predicates.add(cb.le(rewardRateCol, maxRewardRate));
                }
                if (statusList != null && statusList.size() > 0) {
                    predicates.add(statusCol.in(statusList));
                }
                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };

        Page<Product> page = repository.findAll(specification, pageable);

        LOG.debug("查询产品，结果={}", page);
        return page;
    }
}

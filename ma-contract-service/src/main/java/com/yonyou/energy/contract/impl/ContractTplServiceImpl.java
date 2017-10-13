package com.yonyou.energy.contract.impl;

import com.yonyou.energy.common.domain.criteria.BaseCriteria;
import com.yonyou.energy.common.domain.response.BaseResponseCode;
import com.yonyou.energy.common.domain.response.ServiceResponse;
import com.yonyou.energy.common.util.JsonUtil;
import com.yonyou.energy.contract.api.IContractTplService;
import com.yonyou.energy.contract.dao.itf.IContractTemplateDAO;
import com.yonyou.energy.contract.domain.ContractTemplate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tanghe on 2017/9/27.
 */
@Service
public class ContractTplServiceImpl implements IContractTplService {

    @Autowired
    private IContractTemplateDAO contractTemplateDAO;

    Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 查询
     * @param criteria
     * @return
     */
    @Override
    public ServiceResponse<String> list(BaseCriteria criteria) {
        ServiceResponse<String> response = new ServiceResponse<>();
        logger.info("list 列表查询条件： " + JsonUtil.objToString(criteria));
        List result = contractTemplateDAO.queryEntity(criteria);
        response.setResult(JsonUtil.collectionToString(result));
        logger.info("list 列表查询结果： " + JsonUtil.collectionToString(result));
        return response;
    }

    /**
     * 保存
     * @param data
     * @param criteria
     * @return
     */
    @Override
    public ServiceResponse<String> save(List<ContractTemplate> data, BaseCriteria criteria) {
        ServiceResponse<String> result = new ServiceResponse<>();
        logger.info("save start");
        if (data == null) {
            result.setCode(BaseResponseCode.FAILURE.getCode());//状态 1 成功， 0 失败
            result.setMsg("报错数据为空!");
            return result;
        }
        for (ContractTemplate tpl : data) {
            //默认值
            setCommonProp(tpl, criteria);
        }
        int count = 0;
        count = contractTemplateDAO.updateBatch(data);
        if (count > 0) {
            result.setCode(BaseResponseCode.SUCCESS.getCode());
            result.setMsg("保存成功!");
            result.setResult(JsonUtil.collectionToString(data));
        }
        logger.info("save end");
        return result;
    }

    /**
     * 批量删除
     * @param criteria
     * @return
     */
    @Override
    public ServiceResponse<String> deleteBatch(BaseCriteria criteria) {
        ServiceResponse<String> result = new ServiceResponse<>();
        logger.info("deleteBatch start");
        if(criteria.getId()==null && criteria.getIds()==null){
            result.setCode(BaseResponseCode.FAILURE.getCode());//状态 1 成功， 0 失败
            result.setMsg("请选择要删除的数据!");
            return result;
        }

        int count = contractTemplateDAO.deleteBatch(criteria);

        if(count>0){
            result.setCode(BaseResponseCode.SUCCESS.getCode());
            result.setMsg("删除成功!");
        }
        logger.info("deleteBatch end");
        return result;
    }

    /**
     * 启用
     * @param criteria
     * @return
     */
    @Override
    public ServiceResponse<String> open(BaseCriteria criteria) {
        ServiceResponse<String> result = new ServiceResponse<>();
        logger.info("open start");
        if(criteria.getId()==null && criteria.getIds()==null){
            result.setCode(BaseResponseCode.FAILURE.getCode());//状态 1 成功， 0 失败
            result.setMsg("请选择要启用的模板!");
            return result;
        }

        int count = contractTemplateDAO.open(criteria);

        if(count>0){
            result.setCode(BaseResponseCode.SUCCESS.getCode());
            result.setMsg("启用成功!");
        }
        logger.info("open end");
        return result;
    }

    /**
     * 一些公共字段赋值
     */
    private void setCommonProp(ContractTemplate data, BaseCriteria criteria){
        String currDate = DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
        data.setDr(0);
        if(data.getId()==null || data.getId().equals(0l)){
            data.setCreator(1l);
            data.setCreationtime(currDate);
            data.setStatus(0);
        }else{
            data.setModifier(1l);
            data.setModifytime(currDate);
        }
        data.setTs(currDate);
    }
}

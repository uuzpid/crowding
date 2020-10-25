package com.pyx.mapper;

import com.pyx.entity.Emp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpMapper {
    List<Emp> selectAll();
}

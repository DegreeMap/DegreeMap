package com.degreemap.DegreeMap.utils;

import org.springframework.core.convert.converter.Converter;

import com.degreemap.DegreeMap.courseEntities.prerequisites.GradeRequirement;

/**
 * This class contains some helper methods for converting received json to enums.
 * I followed this tutorial: https://www.baeldung.com/spring-enum-request-param
 * (and a few other online sources)
 * 
 * This only works for GradeRequirements.
 * 
 * If you feel there's a better way to do this that I missed feel free to edit it.
 * - Christian
 */
public class EnumConverter implements Converter<String, GradeRequirement> {
    @Override
    public GradeRequirement convert(String source) {
        return GradeRequirement.valueOf(source.toUpperCase());
    }
}
package com.example.Clops.util;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.stereotype.Component;

@Component
public class GeometryUtil {

    private final GeometryFactory geometryFactory;
    private final WKTReader wktReader;
    private final WKTWriter wktWriter;

    public GeometryUtil() {
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        this.wktReader = new WKTReader(geometryFactory);
        this.wktWriter = new WKTWriter();
    }

    /**
     * Конвертирует WKT строку в JTS Geometry
     */
    public Geometry wktToGeometry(String wkt) {
        if (wkt == null || wkt.trim().isEmpty()) {
            return null;
        }

        try {
            return wktReader.read(wkt);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid WKT format: " + wkt, e);
        }
    }

    /**
     * Конвертирует JTS Geometry в WKT строку
     */
    public String geometryToWkt(Geometry geometry) {
        if (geometry == null) {
            return null;
        }
        return wktWriter.write(geometry);
    }

    /**
     * Создает точку из координат
     */
    public Point createPoint(double longitude, double latitude) {
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return geometryFactory.createPoint(coordinate);
    }

    /**
     * Создает линию из массива координат
     */
    public LineString createLineString(double[][] coordinates) {
        Coordinate[] coords = new Coordinate[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            coords[i] = new Coordinate(coordinates[i][0], coordinates[i][1]);
        }
        return geometryFactory.createLineString(coords);
    }

    /**
     * Проверяет валидность WKT строки
     */
    public boolean isValidWkt(String wkt) {
        if (wkt == null || wkt.trim().isEmpty()) {
            return false;
        }

        try {
            wktReader.read(wkt);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
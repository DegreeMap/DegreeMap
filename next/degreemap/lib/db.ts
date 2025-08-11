"use client";

import Dexie, { Table } from "dexie";

export interface DegreeMap {
	id: string;
	name: string;
	updatedAt: number;
	degreeMap: {
        years: Year[]
    };
}

class DegreeDB extends Dexie {
	degreeMaps!: Table<DegreeMap, string>;

	constructor() {
		super("degree-map-maker");
		this.version(1).stores({
			degreeMaps: "id, updatedAt, name" 
		});
	}
}

export const db = new DegreeDB();

/**
 * Insert or update a DegreeMap.
 * @returns Promise<string | number> - The primary key (id) of the saved map.
 */
export async function saveDegreeMap(
	map: Omit<DegreeMap, "updatedAt"> & { updatedAt?: number }
): Promise<string | number> {
	return db.degreeMaps.put({ ...map, updatedAt: Date.now() });
}

/**
 * Get a single DegreeMap by ID.
 * @returns Promise<DegreeMap | undefined> - The found map or undefined if not found.
 */
export async function getDegreeMap(id: string): Promise<DegreeMap | undefined> {
	return db.degreeMaps.get(id);
}

/**
 * List all DegreeMaps, newest first.
 * @returns Promise<DegreeMap[]> - Array of all maps.
 */
export async function listDegreeMaps(): Promise<DegreeMap[]> {
	return db.degreeMaps.orderBy("updatedAt").reverse().toArray();
}

/**
 * Delete a DegreeMap by ID.
 * @returns Promise<void> - Resolves when delete is complete.
 */
export async function deleteDegreeMap(id: string): Promise<void> {
	return db.degreeMaps.delete(id);
}

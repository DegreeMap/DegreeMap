"use client";

import { useLiveQuery } from "dexie-react-hooks";
import { db, listDegreeMaps, saveDegreeMap, deleteDegreeMap} from "./db";

export function useAllDegreeMaps() {
	const maps = useLiveQuery(() => listDegreeMaps(), [], []);
	return maps ?? [];
}

export async function createNewDegreeMap(name = "Untitled Degree Map") {
	const id = crypto.randomUUID();
	const map: DegreeMap = { id, name, updatedAt: Date.now(), degreeMap: { years: [] } };
	await saveDegreeMap(map);
	return id;
}

export async function renameDegreeMap(id: string, name: string) {
	const existing = await db.degreeMaps.get(id);
	if (!existing) return;
	await saveDegreeMap({ ...existing, name });
}

export async function saveContent(id: string, degreeMap: {years: Year[] }) {
	const existing = await db.degreeMaps.get(id);
	if (!existing) return;
	await saveDegreeMap({ ...existing, degreeMap });
}

export { deleteDegreeMap as deleteDegreeMap };

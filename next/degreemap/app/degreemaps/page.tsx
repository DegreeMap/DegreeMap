"use client";

import { ExportButton } from "@/components/db/ExportButton";
import { ImportButton } from "@/components/db/ImportButton";
import { DegreeMapCard } from "@/components/ui/DegreeMapCard";
import { useAllDegreeMaps, createNewDegreeMap, renameDegreeMap, deleteDegreeMap } from "@/lib/useDegreeMaps";

export default function MapsPage() {
	const maps = useAllDegreeMaps();

	return (
		<div className="p-4 space-y-4">
            <div className="flex items-center justify-between">
            	<h1 className="text-2xl font-semibold text-gray-800 pr-5">DegreeMap</h1>
            	<div className="flex items-center gap-2">
            		<button onClick={() => createNewDegreeMap()} className="px-3 py-2 rounded bg-black text-white">
            			New Degree Map
            		</button>
            		{/* <ExportButton /> <ImportButton /> */}
            	</div>
            </div>

            <div className="overflow-x-auto overflow-y-hidden">
                <div className="flex gap-3 pr-2 py-2">
                    {maps.map((map) => (
                        <DegreeMapCard 
                            degreeMap={map} 
                            onRename={renameDegreeMap}
                            onDelete={deleteDegreeMap}
                        />
                    ))}
                </div>
            </div>
		</div>
	);
}
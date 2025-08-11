"use client";

import { ExportButton } from "@/components/db/ExportButton";
import { ImportButton } from "@/components/db/ImportButton";
import { useAllDegreeMaps, createNewDegreeMap, renameDegreeMap, deleteDegreeMap } from "@/lib/useDegreeMaps";
import Link from "next/link";

export default function MapsPage() {
	const maps = useAllDegreeMaps();

	return (
		<div className="p-4 space-y-4">
			<div className="flex gap-2">
				<button onClick={() => createNewDegreeMap()} className="px-3 py-2 rounded bg-black text-white">
					New Degree Map
				</button>
				<ExportButton />
				<ImportButton />
			</div>

			<ul className="space-y-2">
				{maps.map((m) => (
					<li key={m.id} className="flex items-center gap-2">
						<Link href={`/maps/${m.id}`} className="underline">{m.name}</Link>
						<small className="opacity-70">updated {new Date(m.updatedAt).toLocaleString()}</small>
						<button onClick={() => {
							const name = prompt("Rename:", m.name);
							if (name) renameDegreeMap(m.id, name);
						}}>Rename</button>
						<button onClick={() => deleteDegreeMap(m.id)}>Delete</button>
					</li>
				))}
			</ul>
		</div>
	);
}
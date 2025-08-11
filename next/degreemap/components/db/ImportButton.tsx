export const ImportButton: React.FC = ({}) => {
	return (
		<label className="px-3 py-2 rounded border cursor-pointer">
			Import
			<input
				type="file"
				accept="application/json"
				hidden
				onChange={async (e) => {
					const file = e.target.files?.[0];
					if (!file) return;
					const text = await file.text();
					const maps = JSON.parse(text);
					const { db } = await import("@/lib/db");
					await db.transaction("rw", db.degreeMaps, async () => {
						for (const m of maps) await db.degreeMaps.put(m);
					});
					e.currentTarget.value = "";
				}}
			/>
		</label>
	);
}
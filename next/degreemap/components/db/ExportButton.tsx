export const ExportButton: React.FC = ({}) => {
	return (
		<button
			className="px-3 py-2 rounded border"
			onClick={async () => {
				const { listDegreeMaps } = await import("@/lib/db");
				const maps = await listDegreeMaps();
				const blob = new Blob([JSON.stringify(maps, null, 2)], { type: "application/json" });
				const url = URL.createObjectURL(blob);
				const a = document.createElement("a");
				a.href = url;
				a.download = `degree-maps-${new Date().toISOString()}.json`;
				a.click();
				URL.revokeObjectURL(url);
			}}
		>
			Export
		</button>
	);
}
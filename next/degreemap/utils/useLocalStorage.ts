import { useEffect, useRef, useState } from "react";

export function useLocalStorage<T>(key: string, initialValue: T) {
	const isFirst = useRef(true);
	const [value, setValue] = useState<T>(() => {
		try {
			if (typeof window === "undefined") return initialValue;
			const raw = localStorage.getItem(key);
			return raw ? (JSON.parse(raw) as T) : initialValue;
		} catch {
			return initialValue;
		}
	});

	useEffect(() => {
		if (isFirst.current) { isFirst.current = false; return; }
		try {
			localStorage.setItem(key, JSON.stringify(value));
		} catch {}
	}, [key, value]);

	return [value, setValue] as const;
}
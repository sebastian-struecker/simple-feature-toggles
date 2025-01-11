type Inputs = {
    text: string; length: number;
}

export function OverflowText({text, length}: Inputs) {
    return (<>
        {text.length <= length && <div
            className="overflow-x-scroll">{text}</div>}
        {text.length > length && <div className="tooltip" data-tip={text}>
            <div
                className="overflow-x-scroll">{text.slice(0, length - 1)}...
            </div>
        </div>}
    </>)
}

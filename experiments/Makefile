all: RawPktGenerator.c RawPktCapture.c
	gcc RawPktGenerator.c -o generator
	gcc RawPktCapture.c -o capture -lpthread

clean:
	rm -f generator capture

package thamizh.andro.org.diglossia.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Updated by Chongyu on 12/4/2015.
 */
public class CheckAudioFile {
    public class AudioInfo
    {
        public byte NumChannnels;
        public short SampleRate;
    }
    public Boolean readAudioFile(String fileName)
    {
        //fileName = "C:\\Users\\elezhcho\\Desktop\\Sample Audio Files\\3333333333333.wav";
        //fileName = "https://s3-ap-southeast-1.amazonaws.com/cctvarchives/dot cabs/8112_2015_03_20_12_04_17.405366897583008_78.4818344116211.wav";
        int result=0;
        String resultString = "";
        AudioInfo info = new AudioInfo();



        // first we need to read our wav file, so we can get our info:
        Boolean decision = false;
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            byte[] wav = new byte[(int) inputStream.available()];
            inputStream.read(wav);
            info.NumChannnels = wav[22]; //result is 2
            info.SampleRate = (short)wav[24]; //result is 16K

            int BitPerSample = (short)wav[34];
            int BitRate = BitPerSample * info.SampleRate;


        //WebClient urlGrabber = new WebClient();
        //byte[] wav = urlGrabber.DownloadData(fileName);

        //resultString = System.Text.Encoding.UTF8.GetString(wav);

        // then we are going to get our file's info


        //Debug.WriteLine("The number of channel is: " + info.NumChannnels);
        //Debug.WriteLine("The sample rate is: " + info.SampleRate);
        //Debug.WriteLine("The bit rate is: " + BitRate);

        // nr of samples is the length - the 44 bytes that where needed for the offset
        int samples = (wav.length - 44) / 2;

        //Debug.WriteLine("The number of samples is: " + samples);

        // if there are 2 channels, we need to devide the nr of sample in 2
        if (info.NumChannnels == 2)
        {
            samples /= 2;
        }


        //Debug.WriteLine("The whole audio information is: " + wav[0:43]);

        //Decide the length of the buffer, need to be the power of 2
        int FFTLength=0;
        if (samples>65536){
            FFTLength=65536;
        }
        else if (samples>32768){
            FFTLength=32768;
        }
        else if (samples > 16384){
            FFTLength = 16384;
        }
        else if (samples > 8192){
            FFTLength = 8192;
        }
        else
        {
            return true;
        }

        //Debug.WriteLine("FFTLength is:" + FFTLength);


        //Original double[16384];
        double[] leftChannel = new double[FFTLength];
        double[] rightChannel = new double[FFTLength];
        // create the array
        //leftChannel = new List<float>();
        //if (info.NumChannnels == 2) rightChannel = new List<float>();
        //else rightChannel = null;

        int pos = 44; // start of data chunk
        for (int i = 0; i < FFTLength; i++)
        {
            //leftChannel.Add(BitConverter.ToInt16(wav, pos));
            leftChannel[i]=(short)wav[pos];
            pos += 2;
            if (info.NumChannnels == 2)
            {
                //rightChannel.Add(BitConverter.ToInt16(wav, pos));
                rightChannel[i] = (short)wav[pos];
                pos += 2;
            }
        }




        //File.WriteAllLines(@"C:\Users\elezhcho\Desktop\writeTest.txt", leftChannel.Select(d => d.ToString()).ToArray());

        decision = FFT(leftChannel, info.SampleRate, true);


        //resultString = samples.ToString();



        if(fileName=="wav"){
            result = 1;
        }else{
            result=0;
        }        } catch (FileNotFoundException e) {
            decision = false;
            e.printStackTrace();
        } catch (IOException e) {
            decision = false;
            e.printStackTrace();
        }
        return decision;
    }


    public Boolean FFT(double[] data, short SampleRate, boolean forward)
    {
        int n = data.length;
        //Debug.WriteLine("FFT sample size: " + n);
        // checks n is a power of 2 in 2's complement format
        if ((n & (n - 1)) != 0)
            return false;
        n /= 2;    // n is the number of samples

        Reverse(data, n); // bit index data reversal

        // do transform: so single point transforms, then doubles, etc.
        double sign = forward ? B : -B;
        int mmax = 1;
        while (n > mmax)
        {
            int istep = 2 * mmax;
            double theta = sign * Math.PI / mmax;
            double wr = 1, wi = 0;
            double wpr = Math.cos(theta);
            double wpi = Math.sin(theta);
            for (int m = 0; m < istep; m += 2)
            {
                for (int k = m; k < 2 * n; k += 2 * istep)
                {
                    int j = k + istep;
                    double tempr = wr * data[j] - wi * data[j + 1];
                    double tempi = wi * data[j] + wr * data[j + 1];
                    data[j] = data[k] - tempr;
                    data[j + 1] = data[k + 1] - tempi;
                    data[k] = data[k] + tempr;
                    data[k + 1] = data[k + 1] + tempi;
                }
                double t = wr; // trig recurrence
                wr = wr * wpr - wi * wpi;
                wi = wi * wpr + t * wpi;
            }
            mmax = istep;
        }

        // perform data scaling as needed
        Scale(data, n, forward);

        Boolean result = findAve(data,2*n,SampleRate);

        //Write the result to a file
        //System.IO.File.WriteAllBytes(@"C:\Users\elezhcho\Desktop\writeTest.txt", data);
        //File.WriteAllLines(@"C:\Users\elezhcho\Desktop\writeTest2.txt", data.Select(d => d.ToString()).ToArray());
        return result;
    }


    public Boolean findAve(double[] data, int sampleSize, short SampleRate)
    {
        double result = 0;
        //int sampleSize = 6144;
        // the sample rate is 8K, 8192
        int SR = 8192;
        int mulFac = sampleSize / SR / 4;

        //Take the absolute value first
        for (int i = 0; i < sampleSize; i++)
        {
            data[i] = Math.abs(data[i]);
        }

        int numGroup = sampleSize / SR;

        //True Pattern: Group1 > Group 2 > Group 3 > Group 4
        //Relaxiation: Group 3 and Gorup 4 could be similar, Group 1 and 2 could be similiar
        double group1 = 0;
        double group2 = 0;
        double group3 = 0;
        double group4 = 0;

        for (int i = 0; i < SR * mulFac; i++)
        {
            group1 += data[i];
            group2 += data[SR * mulFac + i];
            group3 += data[2 * SR * mulFac + i];
            group4 += data[3 * SR * mulFac + i];
        }

        //Debug.WriteLine("mulFac is:" + mulFac);
        //Debug.WriteLine("Group1 is:" + group1);
        //Debug.WriteLine("Group2 is:" + group2);
        //Debug.WriteLine("Group3 is:" + group3);
        //Debug.WriteLine("Group4 is:" + group4);

		group1 = group1 / (SR * mulFac);
        group2 = group2 / (SR * mulFac);
        group3 = group3 / (SR * mulFac);
        group4 = group4 / (SR * mulFac);

        if (group1 >= group2 && group1 >= group3 && group1 >= group4 && group2 >= group3 && group2 >= group4 && group1 > 5000 && group2 > 3000 && group3 < 3000)
            return true;
        else
            return false;

    }


    /// <summary>
    /// Determine how scaling works on the forward and inverse transforms.
    /// For size N=2^n transforms, the forward transform gets divided by
    /// N^((1-a)/2) and the inverse gets divided by N^((1+a)/2). Common
    /// values for (A,B) are
    ///     ( 0, 1)  - default
    ///     (-1, 1)  - data processing
    ///     ( 1,-1)  - signal processing
    /// Usual values for A are 1, 0, or -1
    /// </summary>
    public int A;

    /// <summary>
    /// Determine how phase works on the forward and inverse transforms.
    /// For size N=2^n transforms, the forward transform uses an
    /// exp(B*2*pi/N) term and the inverse uses an exp(-B*2*pi/N) term.
    /// Common values for (A,B) are
    ///     ( 0, 1)  - default
    ///     (-1, 1)  - data processing
    ///     ( 1,-1)  - signal processing
    /// Abs(B) should be relatively prime to N.
    /// Setting B=-1 effectively corresponds to conjugating both input and
    /// output data.
    /// Usual values for B are 1 or -1.
    /// </summary>
    public int B ;



    static void Reverse(double[] data, int n)
    {
        // bit reverse the indices. This is exercise 5 in section
        // 7.2.1.1 of Knuth's TAOCP the idea is a binary counter
        // in k and one with bits reversed in j
        int j = 0, k = 0; // Knuth R1: initialize
        int top = n / 2;  // this is Knuth's 2^(n-1)
        while (true)
        {
            // Knuth R2: swap - swap j+1 and k+2^(n-1), 2 entries each
            double t = data[j + 2];
            data[j + 2] = data[k + n];
            data[k + n] = t;
            t = data[j + 3];
            data[j + 3] = data[k + n + 1];
            data[k + n + 1] = t;
            if (j > k)
            { // swap two more
                // j and k
                t = data[j];
                data[j] = data[k];
                data[k] = t;
                t = data[j + 1];
                data[j + 1] = data[k + 1];
                data[k + 1] = t;
                // j + top + 1 and k+top + 1
                t = data[j + n + 2];
                data[j + n + 2] = data[k + n + 2];
                data[k + n + 2] = t;
                t = data[j + n + 3];
                data[j + n + 3] = data[k + n + 3];
                data[k + n + 3] = t;
            }
            // Knuth R3: advance k
            k += 4;
            if (k >= n)
                break;
            // Knuth R4: advance j
            int h = top;
            while (j >= h)
            {
                j -= h;
                h /= 2;
            }
            j += h;
        } // bit reverse loop
    }

    void Scale(double[] data, int n, boolean forward)
    {
        // forward scaling if needed
        if ((forward) && (A != 1))
        {
            double scale = Math.pow(n, (A - 1) / 2.0);
            for (int i = 0; i < data.length; ++i)
                data[i] *= scale;
        }

        // inverse scaling if needed
        if ((!forward) && (A != -1))
        {
            double scale = Math.pow(n, -(A + 1) / 2.0);
            for (int i = 0; i < data.length; ++i)
                data[i] *= scale;
        }
    }
}


package com.drexel.cs283.assignment2;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MiniRSA {

    public static long coprime(long c, long m) {
        long e;
        for (e = 2 * m; ; ++e) {
            if (!is_prime(e) && GCD(e, c) == 1 && GCD(e, m) == 1 && e % m != 1) break;
        }
        return e;
    }

    public static HashMap<String, Long> keysFromPrimes(long a, long b) {
        long c = a * b;
        long m = (a - 1) * (b - 1);

        long e = coprime(c, m);

        long d = mod_inverse(e, m);
        HashMap<String, Long> keys = new HashMap<>();
        keys.put("c", c);//shared
        keys.put("e", e);//public
        keys.put("d", d);//private
        return keys;
    }

    public static boolean is_prime(long n) {
        if (n <= 1) return false;
        else if (n <= 3) return true;
        else if (n % 2 == 0 || n % 3 == 0) return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public static void completeKeys(HashMap<String, Long> keys) {
        if (!keys.containsKey("c")) return;
        if (keys.containsKey("e")) {
            long c = keys.get("c");
            long e = keys.get("e");

            long m = totient(c);
            long d = mod_inverse(e, m);

            keys.put("d", d);
            return;
        } else if (keys.containsKey("d")) {
            long c = keys.get("c");
            long d = keys.get("d");

            long m = totient(c);
            long e = mod_inverse(d, m);

            keys.put("e", e);
            return;
        } else return;
    }

    public static BigInteger endecrypt(long msg_or_cipher, long key, long c) {
        BigInteger pow = BigInteger.valueOf(msg_or_cipher);
        return pow.modPow(BigInteger.valueOf(key), BigInteger.valueOf(c));
    }

//    public static int nthPrime(int n) {
//        int candidate, count;
//        for (candidate = 2, count = 0; count < n; ++candidate) {
//            if (isPrime(candidate)) {
//                ++count;
//            }
//        }
//        return candidate - 1;
//    }
//
//    private static boolean isPrime(int n) {
//        if (n % 2 == 0) return n == 2;
//        if (n % 3 == 0) return n == 3;
//        int step = 4, m = (int) Math.sqrt(n) + 1;
//        for (int i = 5; i < m; step = 6 - step, i += step) {
//            if (n % i == 0) {
//                return false;
//            }
//        }
//        return true;
//    }

//    public static int nthPrime(int n) {
//        if (n < 2) return 2;
//        if (n == 2) return 3;
//        int limit, root, count = 1;
//        limit = (int)(n*(Math.log(n) + Math.log(Math.log(n)))) + 3;
//        root = (int)Math.sqrt(limit) + 1;
//        limit = (limit-1)/2;
//        root = root/2 - 1;
//        boolean[] sieve = new boolean[limit];
//        for(int i = 0; i < root; ++i) {
//            if (!sieve[i]) {
//                ++count;
//                for(int j = 2*i*(i+3)+3, p = 2*i+3; j < limit; j += p) {
//                    sieve[j] = true;
//                }
//            }
//        }
//        int p;
//        for(p = root; count < n; ++p) {
//            if (!sieve[p]) {
//                ++count;
//            }
//        }
//        return 2*p+1;
//    }

    //stack overflow fast - needs explanation
    public static int nthPrime(int n) {
        if (n < 2) return 2;
        if (n == 2) return 3;
        if (n == 3) return 5;
        int limit, root, count = 2;
        limit = (int) (n * (Math.log(n) + Math.log(Math.log(n)))) + 3;
        root = (int) Math.sqrt(limit);
        switch (limit % 6) {
            case 0:
                limit = 2 * (limit / 6) - 1;
                break;
            case 5:
                limit = 2 * (limit / 6) + 1;
                break;
            default:
                limit = 2 * (limit / 6);
        }
        switch (root % 6) {
            case 0:
                root = 2 * (root / 6) - 1;
                break;
            case 5:
                root = 2 * (root / 6) + 1;
                break;
            default:
                root = 2 * (root / 6);
        }
        int dim = (limit + 31) >> 5;
        int[] sieve = new int[dim];
        for (int i = 0; i < root; ++i) {
            if ((sieve[i >> 5] & (1 << (i & 31))) == 0) {
                int start, s1, s2;
                if ((i & 1) == 1) {
                    start = i * (3 * i + 8) + 4;
                    s1 = 4 * i + 5;
                    s2 = 2 * i + 3;
                } else {
                    start = i * (3 * i + 10) + 7;
                    s1 = 2 * i + 3;
                    s2 = 4 * i + 7;
                }
                for (int j = start; j < limit; j += s2) {
                    sieve[j >> 5] |= 1 << (j & 31);
                    j += s1;
                    if (j >= limit) break;
                    sieve[j >> 5] |= 1 << (j & 31);
                }
            }
        }
        int i;
        for (i = 0; count < n; ++i) {
            count += popCount(~sieve[i]);
        }
        --i;
        int mask = ~sieve[i];
        int p;
        for (p = 31; count >= n; --p) {
            count -= (mask >> p) & 1;
        }
        return 3 * (p + (i << 5)) + 7 + (p & 1);
    }

    // Count number of set bits in an int
    public static int popCount(int n) {
        n -= (n >>> 1) & 0x55555555;
        n = ((n >>> 2) & 0x33333333) + (n & 0x33333333);
        n = ((n >> 4) & 0x0F0F0F0F) + (n & 0x0F0F0F0F);
        return (n * 0x01010101) >> 24;
    }

    public static long GCD(long a, long b) {
        if (a == 0) {
            return b;
        }
        return GCD(b % a, a);
    }

    public static long mod_inverse(long base, long m) {
        base = base % m;
        for (long x = 1; x < m; x++)
            if ((base * x) % m == 1)
                return x;
        return 0;
    }

    public static long modulo(long a, long b, long c) {
        return (long) (Math.pow(a, b) % c);
    }

    public static long totient(long n) {
        long result = 1;
        for (long i = 2; i < n; ++i) {
            if (GCD(i, n) == 1) {
                result++;
            }
        }
        return result;
    }

    public static String encryptString(String s, Keys withKeys){

        long key = withKeys.getPublicKey();
        long c = withKeys.getSharedKey();

        char[] characters = s.toCharArray();
        long[] encryptedCharacters = new long[characters.length];
        for(int i = 0 ; i < characters.length ; i++){
            encryptedCharacters[i] = endecrypt(characters[i], key, c).longValue();
        }
        String result = Arrays.stream(encryptedCharacters)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(" "));
        return result;
    }

    public static String decryptString(String s, Keys withKeys){

        long key = withKeys.getPrivateKey();
        long c = withKeys.getSharedKey();

        String[] encryptedCharacters = s.split(" ");
        char[] decryptedCharacters = new char[encryptedCharacters.length];
        for(int i = 0 ; i < encryptedCharacters.length ; i++){
            decryptedCharacters[i] = (char) endecrypt(Long.parseLong(encryptedCharacters[i]), key, c).longValue();
        }
        return new String(decryptedCharacters);
    }

    public static Keys generateNewKeys(){

        int r1 = (int) ((Math.random() * 100) + 300);
        int r2 = (int) ((Math.random() * 100) + 400);

        long p = MiniRSA.nthPrime(r1);
        long q = MiniRSA.nthPrime(r2);

        HashMap<String, Long> keys = keysFromPrimes(p, q);

        Keys ret = new Keys();
        ret.setSharedKey(keys.get("c"));
        ret.setPublicKey(keys.get("e"));
        ret.setPrivateKey(keys.get("d"));

        return ret;
    }

    public static void main(String[] args) {
        long p = nthPrime(200);
        long q = nthPrime(300);
        System.out.println("p: " + p + " q: " + q);
        //Create keys from two primes
        HashMap<String, Long> keys = keysFromPrimes(p, q);

        //String before = encryptString("This is a string", keys.get("d"), keys.get("c"));
        //String after = decryptString(before, keys.get("e"), keys.get("c"));
    }
}


